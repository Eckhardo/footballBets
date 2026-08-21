package sportbets.service.tipps.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Competition;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.entity.tipps.TippConfig;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.competition.CompetitionMembershipRepository;
import sportbets.persistence.repository.competition.CompetitionRepository;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.persistence.repository.tipps.TippConfigRepository;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.persistence.repository.tipps.TippRepository;
import sportbets.persistence.rowObject.TippRow;
import sportbets.persistence.rowObject.TippsRow;
import sportbets.service.tipps.TippService;
import sportbets.web.dto.MapperUtilTipps;
import sportbets.web.dto.tipps.TippDto;
import sportbets.web.error.TippValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TippServiceImpl implements TippService {

    private static final Logger log = LoggerFactory.getLogger(TippServiceImpl.class);

    private final ModelMapper modelMapper;
    private final TippModusRepository tippModusRepo;
    private final TippConfigRepository tippConfigRepo;
    private final CommunityMembershipRepository commMembRepo;
    private final SpielRepository spielRepo;
    private final SpieltagRepository spieltagRepo;
    private final CompetitionRepository compRepo;
    private final CompetitionMembershipRepository compMembRepo;
    private final TippRepository tippRepo;

    public TippServiceImpl(TippModusRepository tippModusRepo, TippConfigRepository tippConfigRepo, CommunityMembershipRepository commMembRepo, SpielRepository spielRepo, SpieltagRepository spieltagRepo, CompetitionRepository compRepo, CompetitionMembershipRepository compMembRepo, TippRepository tippRepo) {
        this.tippConfigRepo = tippConfigRepo;
        this.spieltagRepo = spieltagRepo;
        this.compMembRepo = compMembRepo;
        this.compRepo = compRepo;
        this.modelMapper = new MapperUtilTipps().modelMapperForTipp();
        this.tippModusRepo = tippModusRepo;
        this.commMembRepo = commMembRepo;
        this.spielRepo = spielRepo;
        this.tippRepo = tippRepo;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TippDto> findById(Long id) {
        Tipp entity = tippRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tipp not found"));
        return Optional.of(convertToDto(entity));
    }

    @Override
    @Transactional
    public TippDto saveOne(TippDto dto) {
        log.debug("save tipp");

        Optional<Tipp> entity = tippRepo.findByParents(dto.getCommMembId(), dto.getTippModusId(), dto.getSpielId());
        if (entity.isPresent()) {
            throw new EntityExistsException("Tipp for spielId:" + dto.getSpielId() + " for commMembId " + dto.getCommMembId() + " already exists");
        }
        TippModus tippModus = tippModusRepo.findById(dto.getTippModusId()).orElseThrow(() -> new EntityNotFoundException("tippModus with id" + dto.getSpielId() + " does not exist"));
        Spiel spiel = spielRepo.findById(dto.getSpielId()).orElseThrow(() -> new EntityNotFoundException("spiel with id" + dto.getSpielId() + " does not exist"));
        CommunityMembership commMemb = commMembRepo.findById(dto.getCommMembId()).orElseThrow(() -> new EntityNotFoundException("commMemb with id" + dto.getCommMembId() + "does not exist"));
        Tipp tipp = convertToEntity(dto, spiel, tippModus, commMemb);
        tipp.setCommunityMembership(commMemb);
        tipp.setTippModus(tippModus);
        tipp.setSpiel(spiel);
        boolean isValid = tippModus.isTippValid(tipp);
        if (!isValid) {
            log.error("Tipp  not valid: {}", dto);
            throw new TippValidationException("Tipp is not valid:  " + dto);
        }

        int winPoints = tippModus.calculateWinPoints(tipp, spiel);
        log.info("######## winPoints: {}", winPoints);
        tipp.setWinPoints(winPoints);

        log.debug("tipp to save:{}", tipp);
        Tipp savedEntity = tippRepo.save(tipp);
        return convertToDto(savedEntity);
    }


    @Override
    @Transactional
    public Optional<TippDto> updateOne(Long id, TippDto tippDto) {
        log.debug("update tipp {}", tippDto);

        Tipp entity = tippRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tipp with id:" + tippDto.getId() + "does not exist"));
        TippModus tippModus = tippModusRepo.findById(tippDto.getTippModusId()).orElseThrow(() -> new EntityNotFoundException("tippModus with id" + tippDto.getSpielId() + " does not exist"));

        Spiel spiel = spielRepo.findById(tippDto.getSpielId()).orElseThrow(() -> new EntityNotFoundException("spiel with id" + tippDto.getSpielId() + " does not exist"));
        CommunityMembership commMemb = commMembRepo.findById(tippDto.getCommMembId()).orElseThrow(() -> new EntityNotFoundException("commMemb with id" + tippDto.getCommMembId() + "does not exist"));


        Tipp toUpdate = convertToEntity(tippDto, spiel, tippModus, commMemb);
        entity.setId(id);
        toUpdate.setCommunityMembership(commMemb);
        toUpdate.setSpiel(spiel);
        toUpdate.setTippModus(tippModus);
        boolean isValid = tippModus.isTippValid(toUpdate);
        if (!isValid) {
            throw new TippValidationException("Tipp is not valid");
        }
        int winPoints = tippModus.calculateWinPoints(toUpdate, spiel);
        toUpdate.setWinPoints(winPoints);
        log.debug("tipp to update:{}", toUpdate);
        Tipp updated = tippRepo.save(toUpdate);

        return Optional.of(convertToDto(updated));

    }

    @Override
    @Transactional
    public List<TippDto> saveList(List<TippDto> dtoList) {
        List<TippDto> saved = new ArrayList<>();

        for (TippDto dto : dtoList) {
            log.debug("save dto {}", dto);
            saved.add(this.saveOne(dto));
        }
        return saved;
    }

    @Override
    @Transactional
    public void createOrUpdateRowList(Long spieltagId, List<TippRow> tippRows) {
        log.debug("createOrUpdateRowList for matchday:: {}", spieltagId);
        if (tippRows.isEmpty()) {
            return;
        }
        TippRow row = tippRows.get(0);
        Spieltag matchday;
        if (spieltagId == null) {
            Spiel spiel = spielRepo.findById(row.getSpielId()).orElseThrow(() -> new EntityNotFoundException("spiel with id" + row.getSpielId() + " does not exist"));
            matchday = spiel.getSpieltag();

        } else {
            matchday = spieltagRepo.findById(spieltagId).orElseThrow(() -> new EntityNotFoundException("spieltag with id" + spieltagId + " does not exist"));
        }

        CommunityMembership commMemb = commMembRepo.findById(row.getCommMembId()).orElseThrow(() -> new EntityNotFoundException("commMemb with id" + row.getCommMembId() + "does not exist"));
        Competition comp = compRepo.findByName(row.getCompetitionName()).orElseThrow(() -> new EntityNotFoundException("competition with name" + row.getCompetitionName() + " does not exist"));
        CompetitionMembership compMemb = compMembRepo.findByCommIdAndCompId(commMemb.getCommunity().getId(), comp.getId()).orElseThrow(() -> new EntityNotFoundException("competition membership with community id" + commMemb.getCommunity().getId() + " does not exist"));

        TippConfig config = tippConfigRepo.getTippConfig(matchday.getId(), compMemb.getId());
        TippModus tippModus = tippModusRepo.findById(config.getTippModus().getId()).orElseThrow(() -> new EntityNotFoundException("tippModus with id" + config.getTippModus().getId() + " does not exist"));
        if (spieltagId == null) {
            saveTipps(tippRows, commMemb, tippModus);
        } else {
            updateTipps(tippRows, tippModus);
        }
    }

    private void updateTipps(List<TippRow> tippRows, TippModus tippModus) {
        log.debug("updating tipp rows   {}", tippRows.size());
        for (TippRow updateRow : tippRows) {
            Tipp tipp = tippRepo.findById(updateRow.getTippId()).orElseThrow(() -> new EntityNotFoundException("tipp with id" + updateRow.getTippId() + " does not exist"));
            if (tipp.getId().equals(updateRow.getTippId())) {
                tipp.setHeimTipp(updateRow.getHeimTipp());
                tipp.setRemisTipp(updateRow.getRemisTipp());
                tipp.setGastTipp(updateRow.getGastTipp());
                if (tippModus.isTippValid(tipp)) {
                    log.debug("update tipp   {}", tipp);
                    tippRepo.save(tipp);
                } else {
                    throw new TippValidationException("tipp is not valid");
                }
            }
        }
    }

    private void saveTipps(List<TippRow> tippRows, CommunityMembership commMemb, TippModus tippModus) {
        log.debug("saving tipp rows   {}", tippRows.size());
        for (TippRow createRow : tippRows) {
            log.debug("saving tipp   {}", createRow);
            Spiel spiel = spielRepo.findById(createRow.getSpielId()).orElseThrow(() -> new EntityNotFoundException("spiel with id" + createRow.getSpielId() + " does not exist"));

            Tipp tipp = new Tipp(spiel, commMemb, tippModus, createRow
                    .getHeimTipp(), createRow.getRemisTipp(), createRow
                    .getGastTipp());
            log.debug("save tipp   {}", tipp);
            if (tippModus.isTippValid(tipp)) {
                tippRepo.save(tipp);

            } else {
                throw new TippValidationException("Bet for game "+ spiel.getHeimTeam().getAcronym() + "-"+ spiel.getGastTeam().getAcronym() +" is not valid");
            }

        }
    }


    @Override
    @Transactional
    public List<TippDto> updateList(List<TippDto> dtoList) {
        List<TippDto> updated = new ArrayList<>();

        for (TippDto dto : dtoList) {
            Optional<TippDto> updatedDto = this.updateOne(dto.getId(), dto);
            updatedDto.ifPresent(updated::add);
        }
        return updated;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (tippRepo.existsById(id)) {
            tippRepo.deleteById(id);
        }
    }

    @Override
    public List<TippRow> findEmptyTippRowsForTipper(Long spieltagId) {
        return tippRepo.findEmptyTippRowsForTipper(spieltagId);
    }

    @Override
    public List<TippRow> findTippRowsForTipper(Long spieltagId, Long commMembId) {
        return tippRepo.findTippRowsForTipper(spieltagId, commMembId);
    }

    @Override
    public List<TippsRow> findTippsRowsForCommunity(Long spieltagId, Long commId) {
        return tippRepo.findTippsRowsForCommunity(spieltagId, commId);
    }

    @Override
    public void deleteAll() {
        tippRepo.deleteAll();
    }

    private TippDto convertToDto(Tipp entity) {
        return modelMapper.map(entity, TippDto.class);
    }

    private Tipp convertToEntity(TippDto dto, Spiel spiel, TippModus tippModus, CommunityMembership commMemb) {
        return new Tipp(spiel, commMemb, tippModus, dto.getHeimTipp(), dto.getRemisTipp(), dto.getGastTipp());

    }

}

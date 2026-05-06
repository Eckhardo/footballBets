package sportbets.service.tipps.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sportbets.persistence.entity.community.CommunityMembership;
import sportbets.persistence.entity.competition.Spiel;
import sportbets.persistence.entity.tipps.Tipp;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.repository.community.CommunityMembershipRepository;
import sportbets.persistence.repository.competition.SpielRepository;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.persistence.repository.tipps.TippRepository;
import sportbets.service.tipps.TippService;
import sportbets.web.dto.MapperUtilTipps;
import sportbets.web.dto.tipps.TippDto;
import sportbets.web.error.TippValidationException;

import java.util.InputMismatchException;
import java.util.Optional;


@Service
public class TippServiceImpl implements TippService {

    private static final Logger log = LoggerFactory.getLogger(TippServiceImpl.class);

    private final ModelMapper modelMapper;
    private final MapperUtilTipps mapperUtilTipp;
    private final TippModusRepository tippModusRepo;
    private final CommunityMembershipRepository commMembRepo;
    private final SpielRepository spielRepo;
    private final TippRepository tippRepo;

    public TippServiceImpl(ModelMapper modelMapper, MapperUtilTipps mapperUtilTipp, TippModusRepository tippModusRepo, CommunityMembershipRepository commMembRepo, SpielRepository spielRepo, TippRepository tippRepo) {
        this.modelMapper = modelMapper;
        this.mapperUtilTipp = mapperUtilTipp;
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
    public TippDto save(TippDto dto) {
        log.debug("save tipp");

        Optional<Tipp> entity = tippRepo.findByParents(dto.getCommMembId(), dto.getTippModusId(), dto.getSpielId());
        if (entity.isPresent()) {
            throw new EntityExistsException("Tipp for spielId:" + dto.getSpielId() + " for commMembId " + dto.getCommMembId() + " already exists");
        }
       TippModus tippModus = tippModusRepo.findById(dto.getTippModusId()).orElseThrow(() -> new EntityNotFoundException("tippModus with id" + dto.getSpielId() + " does not exist"));


        boolean isValid= tippModus.isTippValid(dto);
        if (! isValid) {
            log.error("Tipp  not valid: {}", dto);
            throw new TippValidationException("Tipp is not valid:  "+ dto );
        }

        Spiel spiel = spielRepo.findById(dto.getSpielId()).orElseThrow(() -> new EntityNotFoundException("spiel with id" + dto.getSpielId() + " does not exist"));
        int winPoints= tippModus.calculateWinPoints(dto,spiel);
        log.info("######## winPoints: {}", winPoints);
        dto.setWinPoints(winPoints);

        CommunityMembership commMemb = commMembRepo.findById(dto.getCommMembId()).orElseThrow(() -> new EntityNotFoundException("commMemb with id" + dto.getCommMembId() + "does not exist"));
        log.debug("map dto to  tipp entity");
        Tipp tipp = convertToEntity(dto,spiel,tippModus,commMemb);
        tipp.setCommunityMembership(commMemb);
        tipp.setTippModus(tippModus);
        tipp.setSpiel(spiel);
        log.debug("tipp to save:{}", tipp);
        Tipp savedEntity = tippRepo.save(tipp);
        return convertToDto(savedEntity);
    }



    @Override
    @Transactional
    public Optional<TippDto> update(Long id, TippDto dto) {
        log.debug("update tipp {}",dto);

        Tipp entity = tippRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tipp with id:" + dto.getId() + "does not exist"));

        TippModus tippModus = tippModusRepo.findById(dto.getTippModusId()).orElseThrow(() -> new EntityNotFoundException("tippModus with id" + dto.getSpielId() + " does not exist"));


        boolean isValid= tippModus.isTippValid(dto);
        if (! isValid) {
            throw new TippValidationException("Tipp is not valid");
        }


        Spiel spiel = spielRepo.findById(dto.getSpielId()).orElseThrow(() -> new EntityNotFoundException("spiel with id" + dto.getSpielId() + " does not exist"));
        CommunityMembership commMemb = commMembRepo.findById(dto.getCommMembId()).orElseThrow(() -> new EntityNotFoundException("commMemb with id" + dto.getCommMembId() + "does not exist"));
        int winPoints= tippModus.calculateWinPoints(dto,spiel);
        log.info("######## winPoints: {}", winPoints);
        dto.setWinPoints(winPoints);

        Tipp toUpdate =  convertToEntity(dto,spiel,tippModus,commMemb);
        entity.setId(id);
        toUpdate.setCommunityMembership(commMemb);
        toUpdate.setSpiel(spiel);
        toUpdate.setTippModus(tippModus);
        log.debug("before update: {}", toUpdate);
        Tipp updated = tippRepo.save(toUpdate);
        log.debug("after update: {}", updated);
        return Optional.of(convertToDto(updated));

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        tippRepo.deleteById(id);
    }

    private TippDto convertToDto(Tipp entity) {
        ModelMapper myMapper = mapperUtilTipp.modelMapperForTipp();
        return myMapper.map(entity, TippDto.class);
    }
    private Tipp convertToEntity(TippDto dto, Spiel spiel, TippModus tippModus, CommunityMembership commMemb) {
        return new Tipp(spiel,commMemb,tippModus,dto.getHeimTipp(),dto.getRemisTipp(), dto.getGastTipp(),dto.getWinPoints());

    }


}

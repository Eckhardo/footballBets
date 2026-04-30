package sportbets.service.tipps.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sportbets.persistence.entity.competition.CompetitionMembership;
import sportbets.persistence.entity.competition.Spieltag;
import sportbets.persistence.entity.tipps.TippConfig;
import sportbets.persistence.entity.tipps.TippModus;
import sportbets.persistence.repository.competition.CompetitionMembershipRepository;
import sportbets.persistence.repository.competition.SpieltagRepository;
import sportbets.persistence.repository.tipps.TippConfigRepository;
import sportbets.persistence.repository.tipps.TippModusRepository;
import sportbets.persistence.rowObject.TippConfigRow;
import sportbets.service.tipps.TippConfigService;
import sportbets.web.dto.MapperUtilTipps;
import sportbets.web.dto.tipps.TippConfigDto;

import java.util.List;
import java.util.Optional;

@Service
public class TippConfigServiceImpl implements TippConfigService {

    private final TippConfigRepository tippConfigRepo;
    private final ModelMapper modelMapper;
    private final   MapperUtilTipps mapperUtilTipp;
    private final CompetitionMembershipRepository compMembRepo;
    private final SpieltagRepository spieltagRepo;
    private final TippModusRepository tippModusRepo;

    public TippConfigServiceImpl(TippConfigRepository tippConfigRepo, ModelMapper modelMapper, MapperUtilTipps mapperUtilTipp, CompetitionMembershipRepository compMembRepo, SpieltagRepository spieltagRepo, TippModusRepository tippModusRepo) {
        this.tippConfigRepo = tippConfigRepo;
        this.modelMapper = modelMapper;
        this.mapperUtilTipp = mapperUtilTipp;
        this.compMembRepo = compMembRepo;
        this.spieltagRepo = spieltagRepo;
        this.tippModusRepo = tippModusRepo;
    }

    @Override
    public Optional<TippConfigDto> findById(Long id) {

        TippConfig tippConfig = tippConfigRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id:" + id));
        return Optional.of(convertToDto(tippConfig));
    }


    @Override
    public TippConfigDto save(TippConfigDto dto) {

        Optional<TippConfig> entity= tippConfigRepo.findById(dto.getId());
        if (entity.isPresent()) {
            throw new EntityExistsException("Entity with id:" + dto.getId() + " already exists");
        }

        CompetitionMembership compMemb=compMembRepo.findById(dto.getCompMembId()).orElseThrow(() -> new EntityNotFoundException("CompMemb not found with id:" + dto.getCompMembId()));
        Spieltag spieltag = spieltagRepo.findById(dto.getSpieltagId()).orElseThrow(()-> new EntityNotFoundException("Spieltag not found with id:" + dto.getSpieltagId()));
        TippModus tippMModus=tippModusRepo.findById(dto.getTippModusId()).orElseThrow(()-> new EntityNotFoundException("TippModus not found with id:" + dto.getTippModusId()));

        TippConfig toSave= modelMapper.map(dto, TippConfig.class);
        toSave.setCompetitionMembership(compMemb);
        toSave.setSpieltag(spieltag);
        toSave.setTippModus(tippMModus);

        TippConfig saved= tippConfigRepo.save(toSave);

        return convertToDto(saved);

    }


    @Override
    public void deleteById(Long id) {
        tippConfigRepo.deleteById(id);
    }

    @Override
    public List<TippConfigRow> findAllForRound(Long compRoundId, Long compMembId) {
        return tippConfigRepo.findAllForRound(compRoundId,compMembId);
    }

    @Override
    public List<TippConfigRow> findTippConfigRows(Long compMembId) {
        return tippConfigRepo.findTippConfigRows(compMembId);
    }

    @Override
    public Optional<TippConfigRow> findTippConfig(Long spieltagId, Long compMembId) {
        return tippConfigRepo.findTippConfig(spieltagId,compMembId);
    }



    private TippConfigDto convertToDto(TippConfig entity) {
     ModelMapper myMapper=   mapperUtilTipp.modelMapperForTippConfig();
     return myMapper.map(entity, TippConfigDto.class);
    }
}

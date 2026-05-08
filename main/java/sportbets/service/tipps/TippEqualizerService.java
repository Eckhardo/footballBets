package sportbets.service.tipps;

public interface TippEqualizerService {
    void equalizeTippsForMatchday(Long spieltagId);

    void equalizeTippsForCompetition(Long competitionId);
}

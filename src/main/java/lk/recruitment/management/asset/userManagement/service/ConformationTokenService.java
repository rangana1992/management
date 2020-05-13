package lk.recruitment.management.asset.userManagement.service;


import lk.recruitment.management.asset.userManagement.dao.ConformationTokenRepository;
import lk.recruitment.management.asset.userManagement.entity.ConformationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConformationTokenService {
    private final ConformationTokenRepository conformationTokenRepository;

    @Autowired
    public ConformationTokenService(ConformationTokenRepository conformationTokenRepository) {
        this.conformationTokenRepository = conformationTokenRepository;
    }

    public ConformationToken createToken(ConformationToken conformationToken) {
        return conformationTokenRepository.save(conformationToken);
    }

    public ConformationToken findByToken(String token) {
    return conformationTokenRepository.findByToken(token);
    }
}

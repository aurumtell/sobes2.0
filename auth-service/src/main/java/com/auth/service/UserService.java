package com.auth.service;

import com.auth.exception.BusinessException;
import com.auth.exception.MyEntityNotFoundException;
import com.auth.model.entity.CompanyEntity;
import com.auth.model.entity.LevelEntity;
import com.auth.model.entity.ProfessionEntity;
import com.auth.model.entity.UserEntity;
import com.auth.model.request.ProfileRequest;
import com.auth.model.response.ProfileResponse;
import com.auth.repo.CompanyRepository;
import com.auth.repo.LevelRepository;
import com.auth.repo.ProfessionRepository;
import com.auth.repo.UserRepository;
import com.auth.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    ProfessionRepository professionRepository;

    @Autowired
    CompanyRepository companyRepository;

    public ProfileResponse getProfile(UserDetailsImpl user) {
        UserEntity userEntity = userRepository.findById(user.getId()).get();
        return new ProfileResponse(userEntity);
    }

    public ProfileResponse createProfile(ProfileRequest profileRequest, UserDetailsImpl user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new MyEntityNotFoundException(user.getId()));

        if (userEntity.getExperience() == null) {
            // Получение и установка уровня пользователя
            userEntity.setExperience(levelRepository.findByExperience(profileRequest.getExperience())
                    .orElseThrow(() -> new MyEntityNotFoundException(profileRequest.getExperience())));

            System.out.println("create");
            // Обработка профессий пользователя
            profileRequest.getProfessions().forEach(professionName -> {
                ProfessionEntity profession = professionRepository.findByProfession(professionName)
                        .orElseThrow(() -> new MyEntityNotFoundException(professionName));
                userEntity.addProfession(profession);
            });

            // Обработка компаний пользователя
            profileRequest.getCompanies().forEach(companyName -> {
                CompanyEntity company = companyRepository.findByCompany(companyName)
                        .orElseThrow(() -> new MyEntityNotFoundException(companyName));
                userEntity.addCompany(company);
            });
            // Сохранение пользователя и возврат профиля
            userRepository.save(userEntity);
            return new ProfileResponse(userEntity);
        } else {
            throw new BusinessException("Profile already exist");
        }
    }

    @Transactional
    public ProfileResponse updateProfile(ProfileRequest profileRequest, UserDetailsImpl user) {
        UserEntity userEntity = userRepository.findById(user.getId()).get();
        System.out.println("user" +profileRequest.getCompanies());

        if (profileRequest.getCompanies() != null) {
            Set<CompanyEntity> tempCompanies = new HashSet<>(userEntity.getCompanies());
            tempCompanies.forEach(userEntity::removeCompany);
            profileRequest.getCompanies().forEach(companyName -> {
                CompanyEntity company = companyRepository.findByCompany(companyName)
                        .orElseThrow(() -> new MyEntityNotFoundException(companyName));
                userEntity.addCompany(company);
            });
        }
        if (profileRequest.getProfessions() != null) {
            System.out.println(userEntity.getProfessions());
            Set<ProfessionEntity> tempProfessions = new HashSet<>(userEntity.getProfessions());
            tempProfessions.forEach(userEntity::removeProfession);
            profileRequest.getProfessions().forEach(profession -> {
                ProfessionEntity professionEntity = professionRepository.findByProfession(profession)
                        .orElseThrow(() -> new MyEntityNotFoundException(profession));
                userEntity.addProfession(professionEntity);
            });
        }
        if (profileRequest.getExperience() != null) {
            userEntity.setExperience(levelRepository.findByExperience(profileRequest.getExperience())
                    .orElseThrow(() -> new MyEntityNotFoundException(profileRequest.getExperience())));
        }
        // Сохранение пользователя и возврат профиля
        userRepository.save(userEntity);
        return new ProfileResponse(userEntity);
    }

    public ProfileResponse getProfileById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new MyEntityNotFoundException("Profile"));
        return new ProfileResponse(userEntity);
    }

    public List<ProfileResponse> getProfiles() {
        return ProfileResponse.toProfileList(userRepository.findAllByVerified(true));
    }

//    public ProfileResponse updateProfessions(ProfileRequest profileRequest, UserDetailsImpl user) {
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//
//        // Сохранение пользователя и возврат профиля
//        userRepository.save(userEntity);
//        return new ProfileResponse(userEntity);
//    }
//
//    public ProfileResponse updateExperience(ProfileRequest profileRequest, UserDetailsImpl user) {
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        userEntity.setExperience(levelRepository.findByExperience(profileRequest.getExperience())
//                .orElseThrow(() -> new MyEntityNotFoundException(profileRequest.getExperience())));
//
//        // Сохранение пользователя и возврат профиля
//        userRepository.save(userEntity);
//        return new ProfileResponse(userEntity);
//    }

//    @Transactional
//    public ProfileResponse updateProfile(ProfileResponse profileResponse, UserDetailsImpl user){
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        userEntity.setProfile(profileResponse.getProfile());
//        userRepository.save(userEntity);
//        return new ProfileResponse(userEntity);
//    }
//
//    public UserEntity getUser(UserDetailsImpl user) {
//        return userRepository.findById(user.getId())
//                .orElseThrow(() -> new MyEntityNotFoundException(user.getId()));
//    }
//
//    public List<MaterialResponse> getSavedMaterials(UserDetailsImpl user) {
//        MaterialResponse materialResponse = new MaterialResponse();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        return materialResponse.getListMaterialResponces(userEntity.getSavedMaterials().stream().toList(), userEntity);
//    }
//
//    @Transactional
//    public MaterialResponse changeSaveMaterial(Long materialId, Boolean status, UserDetailsImpl user) {
//        MaterialEntity material = materialRepository.findById(materialId).orElseThrow(() -> new MyEntityNotFoundException(materialId));
//        UserEntity userEntity = getUser(user);
//        boolean contains = material.getSavedUsers().contains(userEntity);
//        if (contains && !status) {
//            material.getSavedUsers().remove(userEntity);
//            userEntity.getSavedMaterials().remove(material);
//        } else if (!contains && status) {
//            material.getSavedUsers().add(userEntity);
//            userEntity.getSavedMaterials().add(material);
//        }
//        else {
//            throw new BusinessException("Object already exist or deleted");
//        }
//        materialRepository.save(material);
//        return new MaterialResponse(material, userEntity);

//    public List<ArticleResponse> getSavedArticles(UserDetailsImpl user) {
//        ArticleResponse articleResponse = new ArticleResponse();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        return articleResponse.getListArticleResponces(userEntity.getSavedArticles().stream().toList(), userEntity);
//    }

//    public ProfileResponse changeStatusSubscribtion(Long userId, Boolean status, UserDetailsImpl user) {
//        UserEntity currentUser = userRepository.findById(user.getId()).get();
//        UserEntity publisher = userRepository.findById(userId).get();
//        if (publisher.equals(currentUser)) {
//            return null;
//            // ошибка, чел подписывается сам на себя
//        }
//        if (publisher.getSubscribers().contains(currentUser)) {
//            if (status) {
//                // тут должна быть ошибка, что человек подписывается, когда уже подписан
//                System.out.println("oshibka");
//                return null;
//            } else {
//                publisher.removeSubscriber(currentUser);
//            }
//        } else {
//            if (status) {
//                publisher.addSubscriber(currentUser);
//            } else {
//                // тут должна быть ошибка, что человек отписывается, когда подписки нет
//                return null;
//            }
//        }
//        userRepository.save(publisher);
//        userRepository.save(currentUser);
//        return new ProfileResponse(publisher, publisher.getSubscribers().contains(currentUser));
//    }
//
//    public List<ProfileResponse> getSubscribers(UserDetailsImpl user) {
//        ProfileResponse profileResponse = new ProfileResponse();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        return profileResponse.getListProfileResponces(userEntity.getSubscribers().stream().toList(), userEntity);
//    }
//
//    public List<ProfileResponse> getSubscribtions(UserDetailsImpl user) {
//        ProfileResponse profileResponse = new ProfileResponse();
//        UserEntity userEntity = userRepository.findById(user.getId()).get();
//        return profileResponse.getListProfileResponces(userEntity.getSubscriptions().stream().toList(), userEntity);
//    }
//
//    public List<ProfileResponse> getUserSubscribtions(UserDetailsImpl user, Long userId) {
//        ProfileResponse profileResponse = new ProfileResponse();
//        UserEntity userEntity = userRepository.findById(userId).get();
//        UserEntity currentUser = userRepository.findById(user.getId()).get();
//        return profileResponse.getListProfileResponces(userEntity.getSubscriptions().stream().toList(), currentUser);
//    }
//
//    public List<ProfileResponse> getUserSubscribers(UserDetailsImpl user, Long userId) {
//        ProfileResponse profileResponse = new ProfileResponse();
//        UserEntity userEntity = userRepository.findById(userId).get();
//        UserEntity currentUser = userRepository.findById(user.getId()).get();
//        return profileResponse.getListProfileResponces(userEntity.getSubscribers().stream().toList(), currentUser);
//    }

}

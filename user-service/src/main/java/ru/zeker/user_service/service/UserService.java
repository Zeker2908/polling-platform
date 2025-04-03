package ru.zeker.user_service.service;

import ru.zeker.user_service.domain.model.User;

public interface UserService {
    User findByEmail(String email);
    User create(User user);

    User update(User user);

    void deleteById(Long id);

    void deleteByEmail(String email);
}

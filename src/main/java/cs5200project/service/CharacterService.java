package cs5200project.service;

import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.GameCharacter;

public class CharacterService {
    private CharacterDao characterDao;

    public CharacterService() {
        this.characterDao = CharacterDao.getInstance();
    }

    public List<GameCharacter> getAllCharacters() throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            return characterDao.getAllCharacters();
        }
    }

    public GameCharacter createCharacter(GameCharacter character) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            return characterDao.create(character);
        }
    }

    public GameCharacter getCharacterById(int characterId) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            return characterDao.getCharacterById(connection, characterId);
        }
    }
} 
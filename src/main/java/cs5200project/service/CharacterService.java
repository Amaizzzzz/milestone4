package cs5200project.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.GameCharacter;

public class CharacterService {
    private static CharacterService instance = null;
    private final CharacterDao characterDao;
    
    protected CharacterService() {
        characterDao = CharacterDao.getInstance();
    }
    
    public static CharacterService getInstance() {
        if(instance == null) {
            instance = new CharacterService();
        }
        return instance;
    }

    public List<GameCharacter> getAllCharacters() throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            return characterDao.getAllCharacters(conn);
        }
    }
    
    public GameCharacter getCharacterById(int id) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            return characterDao.getCharacterById(conn, id);
        }
    }
} 
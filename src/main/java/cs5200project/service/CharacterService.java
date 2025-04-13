package cs5200project.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs5200project.dal.CharacterDao;
import cs5200project.dal.ConnectionManager;
import cs5200project.model.Character;

public class CharacterService {
    public List<Character> getAllCharacters() throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            return CharacterDao.getAllCharacters(conn);
        }
    }
    
    public Character getCharacterById(int id) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            return CharacterDao.getCharacterById(conn, id);
        }
    }
} 
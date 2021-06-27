package it.polito.tdp.PremierLeague.model;

import java.time.LocalDateTime;

public class TestModel {

	public static void main(String[] args) {
	Model m = new Model();
	LocalDateTime data = LocalDateTime.of(2012, 02, 26, 00, 00,00);
	Match mTemp = new Match(32, 3,6,8,2,1, data, "Arsenal", "Tottenham Hotspur");
	m.creaGrafo(mTemp);

	}

}

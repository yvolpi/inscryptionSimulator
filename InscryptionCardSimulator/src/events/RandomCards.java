package events;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cards.Card;
import cards.CardFactory;
import cards.CardPanel;
import frames.Menu;

public class RandomCards  extends JFrame {
	List<CardPanel> cardsPanelsMainDeck = new ArrayList<>();
	private JButton buttonValidate;
	private Menu menu;
	private boolean deathcards = false;
	
	public RandomCards(Menu menu, Integer nbChoices) throws IOException, FontFormatException {
		super("Random cards");
		this.menu = menu;
		Random r = new Random();
		this.setSize(1530, 650);
		JPanel panel = new JPanel(); 
		panel.setLayout(null);
		List<Card> boosterMain = new ArrayList<>();
		for(int i=0;i<nbChoices;i++) {
			Card card1 = CardFactory.mainCard(
					menu.getModulo1(),
					menu.getMultiplier1(),
					menu.getGlobalStrenght1(),
					menu.getRarityStrenght1(),
					r.nextInt(menu.getModulo1()-1)+1,
					menu.getTypecards1());
			Card card2 = CardFactory.mainCard(
					menu.getModulo1(),
					menu.getMultiplier1(),
					menu.getGlobalStrenght1(),
					menu.getRarityStrenght1(),
					r.nextInt(menu.getModulo1()-1)+1,
					menu.getTypecards1());
			if (card2.getRarity() < card1.getRarity()) {
				boosterMain.add(card2);
			} else {
				boosterMain.add(card1);
			}
		}
		
		
		
		for(int i=0;i<boosterMain.size();i++) {
			cardsPanelsMainDeck.add(new CardPanel(boosterMain.get(i)));
			panel.add(cardsPanelsMainDeck.get(i));
			cardsPanelsMainDeck.get(i).setBounds(200*i, 0, 200, 300);
		}
		
		buttonValidate = new JButton("Validate");
		buttonValidate.setBounds(500,400,100,50);
		buttonValidate.setEnabled(false);
		panel.add(buttonValidate);
		panel.setBounds(0, 0, 200 * cardsPanelsMainDeck.size(), 650);
		panel.setPreferredSize(new Dimension(200 * cardsPanelsMainDeck.size(), 650));
		
		JScrollPane jscrollpane = new JScrollPane(panel);
		
		this.add(jscrollpane, BorderLayout.CENTER);
		
		this.setVisible(true);
		new RandomCardsControler(this);
	}
	
	public RandomCards(Menu menu, Integer nbChoices, boolean deathcards) throws IOException, FontFormatException {
		super("Random death cards");
		this.menu = menu;
		this.deathcards = deathcards;
		Random r = new Random();
		this.setSize(1530, 650);
		JPanel panel = new JPanel(); 
		panel.setLayout(null);
		List<Card> boosterMain = new ArrayList<>();
		int nbDeathCards = menu.getAvailableDeadCardsList().size();
		if (nbDeathCards>0) {
			for(int i=0;i<nbChoices;i++) {
				Card card = menu.getAvailableDeadCardsList().get(r.nextInt(nbDeathCards));
				if (!boosterMain.contains(card)) {
					boosterMain.add(card);
				}
			}
		}
		for(int i=0;i<boosterMain.size();i++) {
			cardsPanelsMainDeck.add(new CardPanel(boosterMain.get(i)));
			panel.add(cardsPanelsMainDeck.get(i));
			cardsPanelsMainDeck.get(i).setBounds(200*i, 0, 200, 300);
		}
		if (nbDeathCards == 0) {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File("conthrax-sb.ttf"));
			JLabel description = new JLabel("There is no any death card available.");
			description.setBounds(50, 310, 600, 50);
			description.setFont(font.deriveFont(Font.BOLD,18f));
			panel.add(description);
		}
		buttonValidate = new JButton("Validate");
		buttonValidate.setBounds(500,400,100,50);
		buttonValidate.setEnabled(nbDeathCards == 0);
		panel.add(buttonValidate);
		panel.setBounds(0, 0, 200 * cardsPanelsMainDeck.size(), 650);
		panel.setPreferredSize(new Dimension(200 * cardsPanelsMainDeck.size(), 650));
		
		JScrollPane jscrollpane = new JScrollPane(panel);
		
		this.add(jscrollpane, BorderLayout.CENTER);
		
		this.setVisible(true);
		new RandomCardsControler(this);
	}
	
	public RandomCards(Menu menu, boolean rarityGaranty, Integer nbChoices) throws IOException, FontFormatException {
		super("Random cards");
		this.menu = menu;
		Random r = new Random();
		this.setSize(1530, 650);
		JPanel panel = new JPanel(); 
		panel.setLayout(null);
		List<Card> boosterMain = new ArrayList<>();
		for(int i=0;i<nbChoices;i++) {
			Card card1 = CardFactory.mainCard(
					menu.getModulo1(),
					menu.getMultiplier1(),
					menu.getGlobalStrenght1(),
					menu.getRarityStrenght1(),
					r.nextInt(menu.getModulo1()-1)+1,
					menu.getTypecards1(),0,rarityGaranty);
			Card card2 = CardFactory.mainCard(
					menu.getModulo1(),
					menu.getMultiplier1(),
					menu.getGlobalStrenght1(),
					menu.getRarityStrenght1(),
					r.nextInt(menu.getModulo1()-1)+1,
					menu.getTypecards1(),0,rarityGaranty);
			if (card2.getRarity() < card1.getRarity()) {
				boosterMain.add(card2);
			} else {
				boosterMain.add(card1);
			}
		}
		
		
		
		for(int i=0;i<boosterMain.size();i++) {
			cardsPanelsMainDeck.add(new CardPanel(boosterMain.get(i)));
			panel.add(cardsPanelsMainDeck.get(i));
			cardsPanelsMainDeck.get(i).setBounds(200*i, 0, 200, 300);
		}
		
		buttonValidate = new JButton("Validate");
		buttonValidate.setBounds(500,400,100,50);
		buttonValidate.setEnabled(false);
		panel.add(buttonValidate);
		panel.setBounds(0, 0, 200 * cardsPanelsMainDeck.size(), 650);
		panel.setPreferredSize(new Dimension(200 * cardsPanelsMainDeck.size(), 650));
		
		JScrollPane jscrollpane = new JScrollPane(panel);
		
		this.add(jscrollpane, BorderLayout.CENTER);
		
		this.setVisible(true);
		new RandomCardsControler(this);
	}

	public List<CardPanel> getCardsPanelsMainDeck() {
		return cardsPanelsMainDeck;
	}

	public void setCardsPanelsMainDeck(List<CardPanel> cardsPanelsMainDeck) {
		this.cardsPanelsMainDeck = cardsPanelsMainDeck;
	}

	public JButton getButtonValidate() {
		return buttonValidate;
	}

	public void setButtonValidate(JButton buttonValidate) {
		this.buttonValidate = buttonValidate;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public boolean isDeathcards() {
		return deathcards;
	}

	public void setDeathcards(boolean deathcards) {
		this.deathcards = deathcards;
	}
	
	

}

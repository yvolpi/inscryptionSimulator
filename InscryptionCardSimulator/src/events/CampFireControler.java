package events;

import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;

import cards.BeastCard;
import cards.Card;
import cards.CardFactory;
import cards.RobotCard;
import events.RandomCards;
import frames.menubuttons.ButtonToDuel;
import frames.menubuttons.ButtonToSimulatorCard;
import frames.duelbuttons.CardPanel;

public class CampFireControler implements ActionListener,MouseListener {
	private CampFire campFire;
	private CardPanel selectedCard = null;
	
	public CampFireControler(CampFire campFire) {
		this.campFire = campFire;
		//menu.getButtonduel().addActionListener(this);
		for (int i=0;i<campFire.getCardsPanelsMainDeck().size();i++) {
			campFire.getCardsPanelsMainDeck().get(i).addMouseListener(this);
		}
		campFire.getButtonValidate().addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("mouseClicked");
		// TODO Auto-generated method stub
		if (e.getSource() instanceof CardPanel) {
			selectedCard = (CardPanel) e.getSource();
			//((CardPanel) e.getSource()).getSelected().setVisible(true);
			int index = campFire.getCardsPanelsMainDeck().indexOf(selectedCard);
			for (int i=0;i<campFire.getCardsPanelsMainDeck().size();i++) {
				campFire.getCardsPanelsMainDeck().get(i).getSelected().setVisible(i == index);
			}
			campFire.getButtonValidate().setEnabled(true);
		}
		
		if (e.getSource() instanceof JButton) {
			Random r = new Random();
			if (r.nextInt(Integer.parseInt(campFire.getChanceDice().getText())) < Integer.parseInt(campFire.getChanceSuccess().getText())) {
				if (campFire.isAttackBonus()) {
					selectedCard.getCard().setAttackBase(selectedCard.getCard().getAttackBase()+1);
					selectedCard.getCard().setAttack(selectedCard.getCard().getAttackBase());
				} else {
					selectedCard.getCard().setHpBase(selectedCard.getCard().getHpBase()+2);
					selectedCard.getCard().setHp(selectedCard.getCard().getHpBase());
				}
			} else {
				campFire.getMenu().getMainDeck1().remove(selectedCard.getCard());
			}
			campFire.getMenu().saveDeck(campFire.getMenu().getMainDeck1(), campFire.getMenu().getSourceDeck1());
			campFire.dispose();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

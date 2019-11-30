// %4263391236:hoplugins.trainingExperience.ui%
package module.training.ui;

import core.constants.player.PlayerAbility;
import core.constants.player.PlayerSkill;
import core.gui.comp.panel.LazyImagePanel;
import core.model.HOVerwaltung;
import core.model.UserParameter;
import core.model.player.FuturePlayer;
import core.training.FutureTrainingManager;
import module.training.Skills;
import module.training.ui.comp.ColorBar;
import module.training.ui.model.ModelChange;
import module.training.ui.model.ModelChangeListener;
import module.training.ui.model.TrainingModel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel where the future training predictions are shown.
 * 
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class PlayerDetailPanel extends LazyImagePanel {

	private static final long serialVersionUID = -6606934473344186243L;
	private JLabel playerLabel;
	private ColorBar[] levelBar;
	private JLabel[] skillLabel;
	private final TrainingModel model;

	/**
	 * Creates the panel and its components
	 */
	public PlayerDetailPanel(TrainingModel model) {
		this.model = model;
	}

	@Override
	protected void initialize() {
		initComponents();
		addListeners();
		loadFromModel();
		setNeedsRefresh(false);
	}

	@Override
	protected void update() {
		loadFromModel();
	}

	private void addListeners() {
		this.model.addModelChangeListener(new ModelChangeListener() {

			@Override
			public void modelChanged(ModelChange change) {
				setNeedsRefresh(true);
			}
		});
	}

	/**
	 * Method that populate this panel for the selected player
	 * 
	 *
	 */
	private void loadFromModel() {
		if (this.model.getActivePlayer() == null) {
			playerLabel.setText(HOVerwaltung.instance().getLanguageString("PlayerSelect"));
			for (int i = 0; i < 8; i++) {
				skillLabel[i].setText("");
				levelBar[i].setLevel(0f);
			}
			return;
		}

		// sets player number
		playerLabel.setText(this.model.getActivePlayer().getName());

		// instantiate a future train manager to calculate the previsions */
		FutureTrainingManager ftm = this.model.getFutureTrainingManager();

		for (int i = 0; i < 8; i++) {
			int skillIndex = Skills.getSkillAtPosition(i);
			skillLabel[i].setText(PlayerAbility.getNameForSkill(
					Skills.getSkillValue(this.model.getActivePlayer(), skillIndex), true));

			FuturePlayer fp = ftm.previewPlayer(UserParameter.instance().futureWeeks);
			double finalValue = getSkillValue(fp, skillIndex);
			levelBar[i].setLevel((float) finalValue / getSkillMaxValue(i));
		}
	}

	/**
	 * Get maximum value of the skill.
	 * 
	 * @param index
	 * 
	 * @return float Max value
	 */
	private float getSkillMaxValue(int index) {
		if (index == 7) {
			return 9f;
		} else {
			return 20f;
		}
	}

	private double getSkillValue(FuturePlayer spieler, int skillIndex) {
		switch (skillIndex) {
		case PlayerSkill.KEEPER:
			return spieler.getGoalkeeping();

		case PlayerSkill.SCORING:
			return spieler.getAttack();

		case PlayerSkill.DEFENDING:
			return spieler.getDefense();

		case PlayerSkill.PASSING:
			return spieler.getPassing();

		case PlayerSkill.PLAYMAKING:
			return spieler.getPlaymaking();

		case PlayerSkill.SET_PIECES:
			return spieler.getSetpieces();

		case PlayerSkill.STAMINA:
			return spieler.getStamina();

		case PlayerSkill.WINGER:
			return spieler.getCross();
		default:
			return 0;
		}
	}

	/**
	 * Initialize the object layout
	 */
	private void initComponents() {
		setOpaque(false);
		setLayout(new GridBagLayout());

		GridBagConstraints maingbc = new GridBagConstraints();
		maingbc.anchor = GridBagConstraints.NORTH;
		maingbc.insets = new Insets(10, 10, 5, 10);
		playerLabel = new JLabel("", SwingConstants.CENTER);
		add(playerLabel, maingbc);

		JPanel bottom = new JPanel(new GridBagLayout());
		bottom.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(2, 4, 2, 4);
		levelBar = new ColorBar[8];
		skillLabel = new JLabel[8];

		for (int i = 0; i < 8; i++) {
			gbc.gridy = i;
			gbc.weightx = 0.0;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			int skillIndex = Skills.getSkillAtPosition(i);
			gbc.gridx = 0;
			bottom.add(new JLabel(PlayerSkill.toString(skillIndex)), gbc);

			skillLabel[i] = new JLabel("");
			skillLabel[i].setOpaque(false);
			gbc.gridx = 1;
			bottom.add(skillLabel[i], gbc);

			levelBar[i] = new ColorBar(0f, 200, 16);
			levelBar[i].setOpaque(false);
			levelBar[i].setMinimumSize(new Dimension(200, 16));
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 2;
			gbc.weightx = 1.0;
			bottom.add(levelBar[i], gbc);
		}
		JPanel dummyPanelToConsumeAllExtraSpace = new JPanel();
		dummyPanelToConsumeAllExtraSpace.setOpaque(false);
		gbc.gridy++;
		gbc.weighty = 1.0;
		bottom.add(dummyPanelToConsumeAllExtraSpace, gbc);

		maingbc.gridy = 1;
		maingbc.insets = new Insets(0, 0, 0, 0);
		maingbc.fill = GridBagConstraints.BOTH;
		maingbc.weightx = 1.0;
		maingbc.weighty = 1.0;
		add(bottom, maingbc);
	}
}

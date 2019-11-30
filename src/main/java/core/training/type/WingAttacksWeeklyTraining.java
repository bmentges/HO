package core.training.type;

import java.util.List;

import core.constants.TrainingType;
import core.constants.player.PlayerSkill;
import core.model.StaffMember;
import core.model.UserParameter;
import core.model.player.IMatchRoleID;
import core.model.player.Player;
import core.training.WeeklyTrainingType;

public class WingAttacksWeeklyTraining extends WeeklyTrainingType {
	protected static WingAttacksWeeklyTraining m_ciInstance = null;
	private WingAttacksWeeklyTraining()
	{
		_Name = "Wing Attacks";
		_TrainingType = TrainingType.WING_ATTACKS;
		_PrimaryTrainingSkill = PlayerSkill.WINGER;
		_PrimaryTrainingSkillPositions = new int[]{ IMatchRoleID.rightWinger, IMatchRoleID.leftWinger,
				IMatchRoleID.leftForward, IMatchRoleID.centralForward, IMatchRoleID.rightForward };
		_PrimaryTrainingSkillOsmosisTrainingPositions = new int[] { IMatchRoleID.keeper, IMatchRoleID.leftBack,
				IMatchRoleID.rightBack, IMatchRoleID.leftCentralDefender,
				IMatchRoleID.middleCentralDefender, IMatchRoleID.rightCentralDefender,
				IMatchRoleID.leftInnerMidfield, IMatchRoleID.centralInnerMidfield,
				IMatchRoleID.rightInnerMidfield};
		_PrimaryTrainingBaseLength = CrossingWeeklyTraining.instance().getBaseTrainingLength(); // old was (float) 2.2 / (float) 0.6
		_PrimaryTrainingSkillBaseLength = (_PrimaryTrainingBaseLength + UserParameter.instance().TRAINING_OFFSET_WINGER) / (float) 0.6 ;
	}
	public static WeeklyTrainingType instance() {
        if (m_ciInstance == null) {
        	m_ciInstance = new WingAttacksWeeklyTraining();
        }
        return m_ciInstance;
    }
	@Override
	public double getTrainingLength(Player player, int assistants, int trainerLevel, int intensity, int stamina, List<StaffMember> staff) {
		return calcTraining(getPrimaryTrainingSkillBaseLength(), player.getAlter(), assistants, trainerLevel, 
				intensity, stamina, player.getWIskill(), staff);
	}
	@Override
	public double getSecondaryTrainingLength(Player player, int assistants, int trainerLevel, int intensity, int stamina, List<StaffMember> staff)
	{
		return (double) -1;
	}
}

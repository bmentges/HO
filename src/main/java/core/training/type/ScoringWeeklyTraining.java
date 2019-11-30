package core.training.type;

import java.util.List;

import core.constants.TrainingType;
import core.constants.player.PlayerSkill;
import core.model.StaffMember;
import core.model.UserParameter;
import core.model.player.IMatchRoleID;
import core.model.player.Player;
import core.training.WeeklyTrainingType;

public class ScoringWeeklyTraining extends WeeklyTrainingType {
	protected static ScoringWeeklyTraining m_ciInstance = null;
	private ScoringWeeklyTraining()
	{
		_Name = "Scoring";
		_TrainingType = TrainingType.SCORING;
		_PrimaryTrainingSkill = PlayerSkill.SCORING;
		_PrimaryTrainingSkillPositions = new int[]{ 
				IMatchRoleID.leftForward, IMatchRoleID.rightForward, IMatchRoleID.centralForward };
		_PrimaryTrainingSkillOsmosisTrainingPositions = new int[]{
				IMatchRoleID.keeper, IMatchRoleID.leftBack, IMatchRoleID.rightBack,
				IMatchRoleID.leftCentralDefender, IMatchRoleID.middleCentralDefender,
				IMatchRoleID.rightCentralDefender, IMatchRoleID.rightWinger,
				IMatchRoleID.leftWinger, IMatchRoleID.leftInnerMidfield,
				IMatchRoleID.centralInnerMidfield, IMatchRoleID.rightInnerMidfield};
		_PrimaryTrainingBaseLength = (float) 4.8536; // old was 3.2
		_PrimaryTrainingSkillBaseLength = _PrimaryTrainingBaseLength + UserParameter.instance().TRAINING_OFFSET_SCORING; // 100%
		//_PrimaryTrainingSkillSecondaryLengthRate = (float) 2; // 50% there are no secondary training positions
	}
	public static WeeklyTrainingType instance() {
        if (m_ciInstance == null) {
        	m_ciInstance = new ScoringWeeklyTraining();
        }
        return m_ciInstance;
    }
	@Override
	public double getTrainingLength(Player player, int assistants, int trainerLevel, int intensity, int stamina, List<StaffMember> staff)
	{
		return calcTraining(getPrimaryTrainingSkillBaseLength(), player.getAlter(), assistants, trainerLevel, 
				intensity, stamina, player.getSCskill(), staff);
	}
	@Override
	public double getSecondaryTrainingLength(Player player, int assistants, int trainerLevel, int intensity, int stamina, List<StaffMember> staff)
	{
		return (double) -1;
	}
}

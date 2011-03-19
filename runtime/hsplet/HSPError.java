/*
 * $Id: HSPError.java,v 1.2 2006/01/13 20:32:10 Yuki Exp $
 */
package hsplet;

/**
 * HSP �̃G���[�l�B
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:10 $
 */
public final class HSPError {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: HSPError.java,v 1.2 2006/01/13 20:32:10 Yuki Exp $";
	
	/** �G���[���� */
	public static final int None = 0;

	/** �V�X�e���G���[���������܂��� */
	public static final int SystemError = 1;

	/** ���@���Ԉ���Ă��܂� */
	public static final int InvalidGrammar = 2;
	
	/** �p�����[�^�̒l���ُ�ł� */
	public static final int InvalidParameterValue = 3;

	/** �v�Z���ŃG���[���������܂��� */
	public static final int ErrorOnExpression = 4;

	/** �p�����[�^�̏ȗ��͂ł��܂��� */
	public static final int ParameterCannotBeOmitted = 5;

	/** �p�����[�^�̌^���Ⴂ�܂� */
	public static final int ParameterTypeMismatch = 6;

	/** �z��̗v�f�������ł� */
	public static final int IndexOutOfBounds = 7;
	
	/** �L���ȃ��x�����w�肳��Ă��܂��� */
	public static final int InvalidLabel = 8;
	
	/** �T�u���[�`���⃋�[�v�̃l�X�g���[�����܂� */
	public static final int SubroutineNestTooDeep = 9;
	
	/** �T�u���[�`���O��return�͖����ł� */
	public static final int ReturnOutOfSubroutineDisallowed = 10;

	/** repeat�O�ł�loop�͖����ł� */
	public static final int LoopOutOfRepeatDisallowed = 11;

	/** �t�@�C����������Ȃ��������Ȗ��O�ł� */
	public static final int FileNotFound = 12;
	
	/** �摜�t�@�C��������܂��� */
	public static final int ImageNotFound = 13;

	/** �O���t�@�C���Ăяo�����̃G���[�ł� */
	public static final int ErrorOnExecution = 14;

	/** �v�Z���ŃJ�b�R�̋L�q���Ⴂ�܂� */
	public static final int InvalidParenthesis = 15;

	/** �p�����[�^�̐����������܂� */
	public static final int ParameetersTooMany = 16;
	
	/** �����񎮂ň����镶�������z���܂��� */
	public static final int StringTooLong = 17;
	
	/** ����ł��Ȃ��ϐ������w�肵�Ă��܂� */
	public static final int CannotAssign = 18;
	
	/** 0�ŏ��Z���܂��� */
	public static final int DividedByZero = 19;
	
	/** �o�b�t�@�I�[�o�[�t���[���������܂��� */
	public static final int BufferOverFlow = 20;
	
	/** �T�|�[�g����Ȃ��@�\��I�����܂��� */
	public static final int UnsupportedOperation = 21;
	
	/** �v�Z���̃J�b�R���[�����܂� */
	public static final int ParenthesisToMany = 22;
	
	/** �ϐ������w�肳��Ă��܂��� */
	public static final int VariableNameNotSpecified = 23;

	/** �����ȊO���w�肳��Ă��܂� */
	public static final int NonIntegerValueSpecified = 24;

	/** �z��̗v�f�������Ԉ���Ă��܂� */
	public static final int InvalidFormOfArray = 25;

	/** �������̊m�ۂ��ł��܂���ł��� */
	public static final int OutOfMemory = 26;
	
	/** �^�C�v�̏������Ɏ��s���܂��� */
	public static final int FailToInitializeType = 27;

	/** �֐��Ɉ������ݒ肳��Ă��܂��� */
	public static final int ParameterNotSpecified = 28;

	/** �X�^�b�N�̈�̃I�[�o�[�t���[�ł� */
	public static final int StackOverFlow = 29;

	/** �����Ȗ��O���p�����[�^�[�Ɏw�肳��Ă��܂� */
	public static final int InvalidNameToParameter = 30;

	/** �قȂ�^�����z��ϐ��ɑ�����܂��� */
	public static final int AssignToDifferentType = 31;
	
	/** �֐��̃p�����[�^�[�L�q���s���ł� */
	public static final int InvalidFunctionDescription = 32;

	/** �I�u�W�F�N�g�����������܂� */
	public static final int ObjectsTooMany = 33;

	/** �z��E�֐��Ƃ��Ďg�p�ł��Ȃ��^�ł� */
	public static final int TypeCannotUseToArrayOrFunction = 34;

	/** ���W���[���ϐ����w�肳��Ă��܂��� */
	public static final int ModuleVariableNotSpecified = 35;
	
	/** ���W���[���ϐ��̎w�肪�����ł� */
	public static final int InvalidModuleVariableSpecification = 36;

	/** �ϐ��^�̕ϊ��Ɏ��s���܂��� */
	public static final int FailToConvertVariableType = 37;

	/** �O��DLL�̌Ăяo���Ɏ��s���܂��� */
	public static final int FailToLoadExternalLibrary = 38;

	/** �O���I�u�W�F�N�g�̌Ăяo���Ɏ��s���܂��� */
	public static final int FailToCallExternalObject = 39;

	/** �֐��̖߂�l���ݒ肳��Ă��܂���B */
	public static final int ReturnValueNotSpecified = 40;


}

// bootup.cpp : コンソール アプリケーションのエントリ ポイントを定義します。
//

#include "stdafx.h"
#include "resource.h"

int ShowErrorMessage(){

	TCHAR errorMessage[MAX_PATH] = {0};

	LoadString( GetModuleHandle(NULL),
		IDS_NOJRE, errorMessage, sizeof( errorMessage )/sizeof( errorMessage[0] ) );


	MessageBox( NULL, errorMessage, _T("Error"), MB_OK );

	return 1;
}

int APIENTRY _tWinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int nCmdShow)
{
	//---------------
	// カレントディレクトリ変更
	TCHAR moduleName[MAX_PATH] = {0};
	
	GetModuleFileName( NULL, moduleName, MAX_PATH );
	PathRemoveFileSpec( moduleName );
	SetCurrentDirectory( moduleName );

	//---------------
	// JRE のパス

	HKEY hJre = NULL;

	TCHAR jreKey[MAX_PATH] = _T("SOFTWARE\\JavaSoft\\Java Runtime Environment");

	if ( RegOpenKey( HKEY_LOCAL_MACHINE, jreKey, &hJre )!=ERROR_SUCCESS ){
		return ShowErrorMessage();
	}

	TCHAR latestJreVersion[MAX_PATH] = {0};

	TCHAR jreVersion[MAX_PATH] = {0};
	DWORD versionLength;

	for( DWORD i = 0;; ++i){

		versionLength = sizeof( jreVersion )/sizeof( jreVersion[0] );

		if ( RegEnumKey( hJre, i, jreVersion, versionLength )!=ERROR_SUCCESS ){
			break;
		}

		if ( lstrcmp( jreVersion, latestJreVersion )>0 ){
			lstrcpy( latestJreVersion, jreVersion );
		}
	}
	
	if ( jreVersion[2]<_T('4') ){
		return ShowErrorMessage();
	}

	HKEY hLatestJre = NULL;

	if ( RegOpenKey( hJre, jreVersion, &hLatestJre )!=ERROR_SUCCESS ){
		return ShowErrorMessage();
	}

	TCHAR jreExe[MAX_PATH] = {0};
	DWORD jreExeSize = sizeof( jreExe );

	if (RegQueryValueEx( hLatestJre, _T("JavaHome"), NULL, NULL, 
		reinterpret_cast< LPBYTE >( jreExe ), &jreExeSize )!=ERROR_SUCCESS ){
		return ShowErrorMessage();
	}

	lstrcpy( jreExe+lstrlen( jreExe), _T("\\bin\\javaw.exe") );

	RegCloseKey( hLatestJre );
	RegCloseKey( hJre );

	TCHAR cmdline[MAX_PATH*4] = {0};

	wsprintf( cmdline, _T("\"%s\" -jar \"compiler.jar\""), jreExe );

	OutputDebugString( cmdline );

	WinExec( cmdline, SW_SHOW );

	return 0;
}

#if defined(NDEBUG)
void WinMainCRTStartup(){
	_tWinMain( NULL, NULL, NULL, SW_SHOW );
}
#endif
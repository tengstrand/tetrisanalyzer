#ifndef __thread__
#define __thread__

#include <windows.h>
#include <conio.h>

// define the interface
struct IRunnable {
  virtual void run() = 0;
};

// Example from :
//   http://www.codeproject.com/threads/ThreadClass.asp
//   http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dllproc/base/creating_threads.asp

class Thread
{
	public:
		Thread(IRunnable *ptr) { _threadObj = ptr; }
		void start() 
		{
			// use the Win32 API here
			DWORD threadID;
			::CreateThread(0, 0, threadProc, _threadObj, 0, &threadID);
		}

  
	protected:
		// Win32 compatible thread parameter and procedure 
		IRunnable *_threadObj; 
		static unsigned long __stdcall threadProc(void* ptr) 
		{
			((IRunnable*)ptr)->run();
			return 0;
		}
};

#endif

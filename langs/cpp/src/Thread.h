#ifndef __thread__
#define __thread__

#ifdef _WIN32
#include <windows.h>
#endif
#include <thread>

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
#ifdef _WIN32
			DWORD threadID;
			::CreateThread(0, 0, threadProc, _threadObj, 0, &threadID);
#else
			std::thread([this]() {
				if (_threadObj)
					_threadObj->run();
			}).detach();
#endif
		}

  
	protected:
		// Win32 compatible thread parameter and procedure 
		IRunnable *_threadObj; 
#ifdef _WIN32
		static unsigned long __stdcall threadProc(void* ptr) 
		{
			((IRunnable*)ptr)->run();
			return 0;
		}
#endif
};

#endif

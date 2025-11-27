#ifndef PLATFORM_H
#define PLATFORM_H

#ifdef _WIN32
#include <windows.h>
#endif

#include <chrono>
#include <thread>

namespace platform {

inline unsigned long getMilliseconds()
{
#ifdef _WIN32
	return ::GetTickCount();
#else
	using namespace std::chrono;
	return static_cast<unsigned long>(
	    duration_cast<milliseconds>(steady_clock::now().time_since_epoch()).count());
#endif
}

inline void sleepMillis(unsigned long milliseconds)
{
#ifdef _WIN32
	::Sleep(milliseconds);
#else
	std::this_thread::sleep_for(std::chrono::milliseconds(milliseconds));
#endif
}

} // namespace platform

#endif // PLATFORM_H


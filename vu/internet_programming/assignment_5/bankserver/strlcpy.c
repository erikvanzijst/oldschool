#include "strlcpy.h"


size_t strlcpy(char *dst, const char *src, size_t dstsize)
{
  strncpy(dst, src, dstsize);
  dst[dstsize - 1] = 0;
  return strlen(src);
}



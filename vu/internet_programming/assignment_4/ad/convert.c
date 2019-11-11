#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[])
{
  FILE *input;
  FILE *output;
  int byte;
  int byte_count = 0;
  int retval = 0;

  if (argc > 2)
  {
    input = fopen(argv[1], "r");
    if (input)
    {
      output = fopen(argv[2], "w");
      if (output)
      {
        fprintf(output, "#ifndef FALLBACK_JPG_H\n");
        fprintf(output, "#define FALLBACK_JPG_H\n");
        fprintf(output, "static unsigned char fallback_jpg[] = {");
        while (!feof(input) && !retval)
        { 
          byte = fgetc(input);
          if (!feof(input))
          {
            if (byte_count != 0) fprintf(output, ",");
            fprintf(output, "0x%x", byte);
            byte_count++;
          }
          else
          {
            fprintf(output, "};\n");
          }
        }
        fprintf(output, "static size_t fallback_jpg_len = %d;\n", byte_count);
        fprintf(output, "\n");
        fprintf(output, "#endif\n");
        fclose(output);
      }
      else // failed to open output file
      {
        perror("convert");
        retval = -1;
      }
      fclose(input);
    }
    else // failed to open input file
    {
      perror("convert");
      retval = -1;
    }
  }
  else // display usage
  {
    fprintf(stderr, "convert <inputfile> <outputfile>\n");
    retval = -1;
  }

  return retval;
}

      

#ifndef _ARK_MADELINE_H
#define _ARK_MADELINE_H

#ifdef __cplusplus
        extern "C" {
#endif

        std::string generatePedigree (const std::string &pedString, const std::string &columnList); //data file name

        void connect ();

#ifdef __cplusplus
        }
#endif

#endif

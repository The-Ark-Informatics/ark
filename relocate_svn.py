#!/usr/bin/env python
#
## Natural Language Toolkit: update svn repository to new UUID
#
# Copyright (C) 2001-2011 NLTK Project
# Author: Edward Loper <edloper@gradient.cis.upenn.edu>
#         Steven Bird <sb@csse.unimelb.edu.au>
# URL: <http://www.nltk.org/>
# For license information, see LICENSE.TXT
# and messed with locally
# $Id:$

# NB Should work on all platforms, http://www.python.org/doc/2.5.2/lib/os-file-dir.html

import os, stat

from global_replace import update

old_uuid = "2eb7856d-1799-4240-b740-a071f1bb3a04" # NLTK at SourceForge
new_uuid = "2beaffb7-5388-1629-0768-26d37724f1fc" # NLTK at GoogleCode

for root, dirs, files in os.walk('.'):
    if root.endswith('.svn'):
        update(os.path.join(root, 'entries'), old_uuid, new_uuid, True)



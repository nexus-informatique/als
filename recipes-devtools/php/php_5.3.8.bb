require php.inc

LIC_FILES_CHKSUM = "file://LICENSE;md5=cb564efdf78cce8ea6e4b5a4f7c05d97"

PR = "${INC_PR}.0"

SRC_URI += "file://acinclude-xml2-config.patch \
            file://php-m4-divert.patch \
            file://0001-php-don-t-use-broken-wrapper-for-mkdir.patch"

SRC_URI_append_pn-php += "file://iconv.patch \
            file://imap-fix-autofoo.patch \
            file://pear-makefile.patch \
            file://phar-makefile.patch \
            file://php_exec_native.patch \
            "

SRC_URI[md5sum] = "704cd414a0565d905e1074ffdc1fadfb"
SRC_URI[sha256sum] = "a1dd06fd5593e97d9a5bd9818d6501d28d3ee8f09b83f0ec78f7cdfc060f3ea2"

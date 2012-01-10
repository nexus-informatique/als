# Copyright (C) 2012, Julien NOBLET - Nexus-Informatique, All Rights Reserved
DESCRIPTION = "The Asterisk open source software PBX"
HOMEPAGE = "http://www.asterisk.org"
LICENSE = "GPLv2"
PRIORITY = "optional"
SECTION = "console/telephony"
DEPENDS = "speex readline zlib openssl curl popt gnutls sqlite libogg libvorbis"
#RRECOMMENDS_${PN} = "logrotate"
PR = "r0"

SRC_URI="http://downloads.asterisk.org/pub/telephony/asterisk/asterisk-${PV}.tar.gz \
	file://logrotate \
	file://volatiles \
	file://init \
"
#	file://Makefile.patch \
#	file://asterisk-1.4-bugid18301.patch \
#	file://detect_openssl_10.patch "

LIC_FILES_CHKSUM = "file://COPYING;md5=3c6764ffcbe996d1d8f919b393ccdd67"



ARCH_efika="powerpc"
ARCH_dht-walnut="powerpc"
ARCH_magicbox="powerpc"
ARCH_sequoia="powerpc"

INITSCRIPT_NAME = "asterisk"
INITSCRIPT_PARAMS = "defaults 60"

inherit update-rc.d

EXTRA_OECONF =  "--with-ssl=${STAGING_EXECPREFIXDIR} \
			--with-z=${STAGING_EXECPREFIXDIR} \
			--with-termcap=${STAGING_EXECPREFIXDIR} \
			--with-ogg=${STAGING_EXECPREFIXDIR} \
			--with-vorbis=${STAGING_EXECPREFIXDIR} \
			--with-sqlite=${STAGING_EXECPREFIXDIR} \
			--with-popt=${STAGING_EXECPREFIXDIR} \
			--with-ncurses=${STAGING_EXECPREFIXDIR} \
			"

#export NOISY_BUILD=yes

#export ASTCFLAGS = "-fsigned-char -I${STAGING_INCDIR} -DPATH_MAX=4096"
#export ASTLDFLAGS="${LDFLAGS} -lpthread -ldl -lresolv "
export PROC="${ARCH}"

do_configure_prepend() {
	sed -i 's:/var:${localstatedir}:' ${WORKDIR}/logrotate
	sed -i 's:/etc/init.d:${sysconfdir}/init.d:' ${WORKDIR}/logrotate
	sed -i 's:/var:${localstatedir}:' ${WORKDIR}/volatiles

	# Due to menuselect below we want to save off these configures
}

do_configure_append() {
	# Put this back
}

do_compile() {
        (
         # Make sure that menuselect gets build using host toolchain
         unset CC CPP LD CXX CCLD CFLAGS CPPFLAGS LDFLAGS CXXFLAGS RANLIB
         unset CONFIG_SITE
         export ac_cv_prog_PKGCONFIG=No
         ./configure
         oe_runmake
        ) || exit 1
        oe_runmake
}

do_install_append() {
        install -d ${D}${sysconfdir}/init.d/
	install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/asterisk
	install -c -D -m 644 ${WORKDIR}/logrotate ${D}${sysconfdir}/logrotate.d/asterisk
	install -c -D -m 644 ${WORKDIR}/volatiles ${D}${sysconfdir}/default/volatiles/asterisk
}

pkg_postinst_prepend() {
	grep -q asterisk  ${sysconfdir}/group || addgroup --system asterisk
	grep -q asterisk ${sysconfdir}/passwd || adduser --system --home ${localstatedir}/run/asterisk --no-create-home --disabled-password --ingroup asterisk -s ${base_bindir}/false asterisk
	chown -R asterisk:asterisk ${libdir}/asterisk ${localstatedir}/lib/asterisk ${localstatedir}/spool/asterisk ${localstatedir}/log/asterisk ${localstatedir}/run/asterisk ${sysconfdir}/asterisk
}

FILES_${PN} += "${libdir}/asterisk/modules/*"
FILES_${PN}-dbg += "${libdir}/asterisk/modules/.debug \
                    ${localstatedir}/lib/asterisk/*/.debug"

SRC_URI[md5sum] = "6b52336b9dc01eeecb9de2eb2818e127"
SRC_URI[sha256sum] = "0b47de6d786a5a09fa8b457e4cd02e85f86eaeecf13cbc2bffdadf1dd493c83f"

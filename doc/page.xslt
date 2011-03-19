<?xml version="1.0" encoding="Shift_JIS" ?>
<!--
 $Id: page.xslt,v 1.2 2006/01/16 19:34:18 Yuki Exp $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.01" encoding="utf-8" omit-xml-declaration="no" doctype-public="-//W3C//DTD HTML 4.01//EN"
		doctype-system="http://www.w3.org/TR/html4/strict.dtd" indent="yes" />
	<xsl:template name="copy-attributes">
		<xsl:for-each select="./@*">
			<xsl:attribute name="{name(.)}">
				<xsl:value-of select="." />
			</xsl:attribute>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="*" name="default-template">
		<xsl:element name="{name(.)}">
			<xsl:call-template name="copy-attributes" />
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	<xsl:template match="text()">
		<xsl:value-of select="." />
	</xsl:template>
	<!--+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++-->
	<xsl:template match="/">
		<html lang="ja">
			<head>
				<meta name="copyright" content="Copyright (C) 2005-2006 Yuki. All rights are reserved." />
				<meta http-equiv="Content-Style-Type" content="text/css" />
				<meta http-equiv="Content-Script-Type" content="text/javascript" />
				<title><xsl:value-of select="page/@name" /> - HSPLet リファレンス</title>
				<link rel="stylesheet" type="text/css" href="common.css" />
				<link rel="stylesheet" type="text/css" href="page.css" />
			</head>
			<body>
				<xsl:apply-templates select="page/header" />
				<xsl:apply-templates select="page" mode="contents" />
				<xsl:apply-templates select="page" mode="body" />

				<hr />				
				<address>
					HSPLet 3.0 <xsl:text disable-output-escaping="yes">&amp;copy;</xsl:text> 2005-2006 <a href="mailto:yuki@group-finity.com">Yuki</a>.<br />
					Logo Illustration <xsl:text disable-output-escaping="yes">&amp;copy;</xsl:text> 2001-2004 chokko.<br />
					All rights are reserved.<br />
					<a href="http://www.group-finity.com/">http://www.group-finity.com/</a><br />
				</address>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="header">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="page" mode="contents">
		<h2>目次</h2>
		
		<ul>
			<xsl:apply-templates select="paragraph" mode="contents"/>
		</ul>
	</xsl:template>
	
	<xsl:template match="paragraph" mode="contents">
		<li>
			<a href="#{generate-id()}"><xsl:value-of select="@name" /></a>
			<xsl:if test="paragraph">
				<ul>
					<xsl:apply-templates select="paragraph" mode="contents"/>
				</ul>
			</xsl:if>
		</li>
	</xsl:template>
	
	<xsl:template match="page" mode="body">
		<xsl:apply-templates select="paragraph"/>
	</xsl:template>
	
	<xsl:template match="paragraph">
		<xsl:param name="depth" select="2" />
		<hr />
		<xsl:element name="h{$depth}">
			<xsl:attribute name="id"><xsl:value-of select="generate-id()" /></xsl:attribute>
			<xsl:value-of select="@name" />
		</xsl:element>
		
		<xsl:apply-templates select="*|text()">
			<xsl:with-param name="depth" select="$depth+1" />
		</xsl:apply-templates>

	</xsl:template>
	
</xsl:stylesheet>
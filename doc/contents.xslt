<?xml version="1.0" encoding="Shift_JIS" ?>
<!--
 $Id: contents.xslt,v 1.3 2006/01/22 16:00:57 Yuki Exp $
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
				<meta name="copyright" content="Copyright (C) 2004 Yuki. All rights are reserved." />
				<meta http-equiv="Content-Style-Type" content="text/css" />
				<meta http-equiv="Content-Script-Type" content="text/javascript" />
				<title>目次 - HSPLet リファレンス</title>
				<link rel="stylesheet" type="text/css" href="common.css" />
				<link rel="stylesheet" type="text/css" href="contents.css" />
			</head>
			<body>
				<xsl:apply-templates select="/reference" mode="contents" />
				<xsl:apply-templates select="/reference" mode="links" />
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="reference | reference//category | /library | /library//category" mode="contents">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="name()='reference'">
				<h1>目次</h1>

				<ul class="contents">
				<xsl:for-each select="page">
					<li><a target="right" href="{@href}"><xsl:value-of select="@name" /></a></li>
				</xsl:for-each>
				</ul>
			</xsl:when>
			<xsl:otherwise>
			<a href="#{$id}">
				<xsl:value-of select="@name" />
			</a>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="library|category">
			<ul class="contents">
				<xsl:for-each select="library|category">
					<li>
						<xsl:apply-templates select="." mode="contents">
							<xsl:with-param name="id" select="concat( $id, name(), position() )" />
						</xsl:apply-templates>
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="reference//library" mode="contents">
		<xsl:param name="id" />
		<xsl:apply-templates select="document( @href )/*" mode="contents">
			<xsl:with-param name="id" select="$id" />
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="reference | reference//category | /library | /library//category" mode="links">
		<xsl:param name="depth" select="2" />
		<xsl:param name="id" />
		<xsl:if test="name()!='reference'">
			<xsl:element name="h{$depth}">
				<xsl:attribute name="id"><xsl:value-of select="$id" /></xsl:attribute>
					<a target="right" href="functions.html#{$id}">
						<xsl:value-of select="@name" />
					</a>
			</xsl:element>
		</xsl:if>
		<xsl:if test="function">
			<ul class="links">
				<xsl:apply-templates select="function" mode="links" />
			</ul>
		</xsl:if>
		<xsl:for-each select="library|category">
			<xsl:apply-templates select="." mode="links">
				<xsl:with-param name="depth" select="$depth + 1 " />
				<xsl:with-param name="id" select="concat( $id, name(), position() )" />
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="reference//library" mode="links">
		<xsl:param name="id" />
		<xsl:apply-templates select="document( @href )/*" mode="links">
			<xsl:with-param name="id" select="$id" />
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="function" mode="links">
		<li>
			<xsl:choose>
				<xsl:when test="@implement='extend'">
					<span class="supportExtend">◎</span>
				</xsl:when>
				<xsl:when test="@implement='yes'">
					<span class="supportYes">○</span>
				</xsl:when>
				<xsl:when test="@implement='partial'">
					<span class="supportPartial">△</span>
				</xsl:when>
				<xsl:when test="@implement='note'">
					<span class="supportNote">！</span>
				</xsl:when>
				<xsl:when test="@implement='no'">
					<span class="supportNo">×</span>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="remarks">
					<a target="right" href="functions.html#function_{translate(@name, ' ', '_' )}" 
						title="{normalize-space( @summary )}">
						<xsl:value-of select="@name" />
					</a>
				</xsl:when>
				<xsl:otherwise>
						<span title="{normalize-space( @summary )}"><xsl:value-of select="@name" /></span>
				</xsl:otherwise>
			</xsl:choose>
		</li>
	</xsl:template>
	
</xsl:stylesheet>
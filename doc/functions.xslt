<?xml version="1.0" encoding="Shift_JIS" ?>
<!--
 $Id: functions.xslt,v 1.3 2006/01/22 16:00:57 Yuki Exp $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.01" encoding="Shift_JIS" omit-xml-declaration="no" doctype-public="-//W3C//DTD HTML 4.01//EN"
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
				<title>関数リファレンス - HSPLet リファレンス</title>
				<link rel="stylesheet" type="text/css" href="common.css" />
				<link rel="stylesheet" type="text/css" href="functions.css" />
			</head>
			<body>
				<h1>関数リファレンス</h1>
				<xsl:apply-templates select="/reference" mode="contents" />
				<xsl:apply-templates select="/reference" mode="reference" />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="reference | reference//category | /library | /library//category" mode="contents">
		<xsl:param name="id" />
		<xsl:if test="name()!='reference'">
			<a href="#{$id}">
				<xsl:value-of select="@name" />
			</a>
		</xsl:if>
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
	
	<xsl:template match="reference | reference//category | /library | /library//category" mode="reference">
		<xsl:param name="depth" select="2" />
		<xsl:param name="id" />
		<xsl:if test="name()!='reference'">
			<hr />
			<xsl:element name="h{$depth}">
				<xsl:attribute name="id">
					<xsl:value-of select="$id" />
				</xsl:attribute>
				<xsl:value-of select="@name" />
			</xsl:element>
		</xsl:if>
		<xsl:apply-templates select="summary" />
		<xsl:if test="function[remarks]">
			<xsl:apply-templates select="function[remarks]" mode="reference" />
		</xsl:if>
		<xsl:for-each select="library|category">
			<xsl:apply-templates select="." mode="reference">
				<xsl:with-param name="depth" select="$depth + 1 " />
				<xsl:with-param name="id" select="concat( $id, name(), position() )" />
			</xsl:apply-templates>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="reference//library" mode="reference">
		<xsl:param name="id" />
		<xsl:apply-templates select="document( @href )/*" mode="reference">
			<xsl:with-param name="id" select="$id" />
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="function" mode="reference">
		<xsl:if test="position()!=1">
			<hr />
		</xsl:if>
		
		<h5 id="function_{translate( @name, ' ', '_' )}">
			<xsl:value-of select="@name" />
		</h5>
		<p>
			<xsl:value-of select="@summary" />
		</p>
		<h6>構文</h6>
		<div class="functionCode">
			<span>
				<xsl:value-of select="@name" />
			</span>
			<xsl:for-each select="param">
				<xsl:if test="position()!=1">
					<span>,</span>
				</xsl:if>
				&#32;<span class="param">
					<xsl:value-of select="@name" />
				</span>
			</xsl:for-each>
		</div>
		<xsl:if test="param">
			<h6>パラメータ</h6>
			<ul class="params">
				<xsl:for-each select="param">
					<li><span class="name">
							<xsl:value-of select="@name" />
						</span>
						-
						<xsl:apply-templates />
						<xsl:if test="@default">
							<span class="default">
							（省略時 = <span class="defaultValue">
									<xsl:value-of select="@default" />
								</span>）
							</span>
						</xsl:if>
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
		<h6>解説</h6>
		<div class="remarks">
			<xsl:apply-templates select="remarks/*|remarks/text()" />
		</div>
		<xsl:if test="seealso">
			<h6>参照</h6>
			<div class="seealso">
				<xsl:for-each select="seealso">
					<xsl:variable name="name" select="@name" />
					<xsl:if test="not( //function[@name=$name] )">
						<xsl:message terminate="yes">
							<xsl:value-of select="$name" /> not found
						</xsl:message>
					</xsl:if>
					<xsl:if test="position()!=1">｜</xsl:if>
					<a href="#function_{translate( @name, ' ', '_' )}"
						title="{normalize-space( //function[@name=$name]/@summary )}">
						<xsl:value-of select="@name" />
					</a>
				</xsl:for-each>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="see">
		<xsl:variable name="name" select="text()" />
		<xsl:if test="count( //function[@name=$name] )=0" >
			<xsl:message terminate="yes">
				<xsl:value-of select="$name" /> not found
			</xsl:message>
		</xsl:if>
		<a href="#function_{$name}" title="{normalize-space( //function[@name=$name]/@summary )}">
				<xsl:apply-templates />
		</a>
	</xsl:template>

</xsl:stylesheet>
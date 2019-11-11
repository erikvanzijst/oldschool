<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="html"/>

	<xsl:template match="xs:schema">
			<html><head></head>
			<body>
				<ul>
				<xsl:apply-templates select="xs:element"/>
				</ul>
			</body>
			</html>
	</xsl:template>

	<xsl:template match="xs:element">
		<h3>&lt;<xsl:value-of select="@name"/>&gt;</h3>
		<p>extends: <xsl:value-of select="@substitutionGroup"/></p>
		<p>returns: <xsl:value-of select="@type"/></p>
		<xsl:variable name="t" select="@type"/>
		<p>
		<xsl:value-of select="//xs:complexType[@name=$t]/xs:annotation/xs:documentation"/>
		</p>
		<hr/>
	</xsl:template>

</xsl:stylesheet>

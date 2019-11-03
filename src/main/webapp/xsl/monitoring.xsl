<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:monitoring="http://morpho/cloudcard/xsd/v2012_01/Monitoring"  version="1.0">

	<xsl:template match="/">
	
        <xsl:variable name="title">Monitoring USAGE</xsl:variable>
        <html>
            <head>
                <title><xsl:value-of select="$title"/></title>
                <meta http-equiv="Cache-Control" content="no-cache"/>
                <meta http-equiv="Pragma" content="no-cache"/>
                <meta http-equiv="Expires" content="0"/> 
				<style type="text/css"> 
				
				.category {margin-top:0em;margin-bottom:0em;}
				.err {color:red;}
				.warn {color:orange;}
				.ok {color:green;}
				UL {margin-top:0em;margin-bottom:0em;}
				
				</style>
			</head>
            <body>
                <h2><xsl:attribute name="class">title</xsl:attribute><xsl:value-of select="$title"/></h2>
                <!-- output the Application node first -->
                <xsl:apply-templates select="Application"/>
                <!-- Then output the other nodes -->
                <xsl:for-each select="*">
                  <xsl:if test="name() != 'Application'">
                     <xsl:apply-templates select="."/>
                  </xsl:if>
                </xsl:for-each>  
				
            </body>
		</html>
	</xsl:template>
	

    <xsl:template match="monitoring:MonitoringData/*">
        <h3>
            <xsl:attribute name="class">category</xsl:attribute>
            <xsl:value-of select="name()"/>
        </h3>
        <ul><xsl:apply-templates select="*"/></ul>
    </xsl:template>
	
    <xsl:template match="monitoring:MonitoringData/*/*">
        <xsl:variable name="categ" select="name(parent::node())"/>
        <xsl:variable name="n" select="name()"/>
        <xsl:variable name="v" select="."/>
        <!-- determine the css class -->
        <xsl:variable name="cssClass">
            <xsl:choose>
                <xsl:when test="$categ = 'Application' and $n = 'TechnicalConfigLoaded'">
                    <xsl:choose>
                        <xsl:when test="$v = 'true'">ok</xsl:when>
                        <xsl:otherwise>err</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$categ = 'Application' and $n = 'DatabaseConnectionOK'">
                    <xsl:choose>
                        <xsl:when test="$v = 'true'">ok</xsl:when>
                        <xsl:otherwise>err</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$categ = 'Application' and $n = 'SEConnectionOK'">
                    <xsl:choose>
                        <xsl:when test="$v = 'true'">ok</xsl:when>
                        <xsl:otherwise>err</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$categ = 'Application' and $n = 'PKIConnectionOK'">
                    <xsl:choose>
                        <xsl:when test="$v = 'true'">ok</xsl:when>
                        <xsl:otherwise>err</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$categ = 'Application' and $n = 'HealthStatus'">
                    <xsl:choose>
                        <xsl:when test="$v = 'GREEN'">ok</xsl:when>
                        <xsl:when test="$v = 'ORANGE'">warn</xsl:when>
                        <xsl:otherwise>err</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:when test="$categ = 'Application' and $n = 'MaintenanceRequired'">
                    <xsl:choose>
                        <xsl:when test="$v = 'true'">warn</xsl:when>
                        <xsl:otherwise>ok</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise></xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- output the li element with the proper css class-->
        <li>
            <xsl:if test="$cssClass != ''">
                <xsl:attribute name="class"><xsl:value-of select="$cssClass"/></xsl:attribute>
            </xsl:if>
            <xsl:value-of select="$n"/>: <xsl:value-of select="$v"/>
        </li>
    </xsl:template>
	
    <xsl:template match="monitoring:MonitoringData/monitoring:JVM/*">
        <li><xsl:value-of select="name()"/>:
        <xsl:variable name="b" select="."/>
        <xsl:variable name="k" select="ceiling($b div 102.4) div 10"/>
        <xsl:variable name="m" select="ceiling($k div 102.4) div 10"/>
        <xsl:variable name="g" select="ceiling($m div 102.4) div 10"/>
        <xsl:choose>
            <xsl:when test="$g > 1">
                <xsl:value-of select="$g"/> gigabytes (<xsl:value-of select="$b"/> bytes)
            </xsl:when>
            <xsl:when test="$m > 1">
                <xsl:value-of select="$m"/> megabytes (<xsl:value-of select="$b"/> bytes)
            </xsl:when>
            <xsl:when test="$k > 1">
                <xsl:value-of select="$k"/> kilobytes (<xsl:value-of select="$b"/> bytes)
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$b"/> bytes
            </xsl:otherwise>
        </xsl:choose>
        </li>
    </xsl:template>
	
    <xsl:template match="monitoring:MonitoringData/monitoring:Application/monitoring:TechnicalConfigLoaded">
        <li>
			<xsl:variable name="val" select="."/>
			<xsl:choose>
				<xsl:when test="$val = 'true'">
					<xsl:attribute name="class">ok</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class">err</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			Configuration loaded: <xsl:value-of select="$val"/></li>
    </xsl:template>
</xsl:stylesheet>

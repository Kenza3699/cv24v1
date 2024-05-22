<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:cv24="http://univ.fr/cv24">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <!-- Template pour la racine -->
    <xsl:template match="/cv24:cv24">
        <html lang="FR">
        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Details de CV</title>
            <link rel="stylesheet" href="/css/cvDetail.css"/>
        </head>
        <body class="page-cv">
            <div class="container">
                <header class="cv-header">
                    <div class="logo">
                        <img src="/images/logo.png" alt="Logo de l'Université de Rouen" />
                    </div>
                    <div class="title">
                        <h1>JobFounder</h1>
                        <h2>CV de <span><xsl:value-of select="cv24:identite/cv24:prenom"/> <xsl:text> </xsl:text><xsl:value-of select="cv24:identite/cv24:nom"/></span></h2>
                    </div>
                </header>
                <main>
                    <section class="cv-section">
                        <h2>Informations Personnelles</h2>
                        <div class="info-personnelle">
                            <p><strong>Email :</strong> <span><xsl:value-of select="cv24:identite/cv24:mel"/></span></p>
                            <p><strong>Téléphone :</strong> <span><xsl:value-of select="cv24:identite/cv24:tel"/></span></p>
                            <p><strong>Genre :</strong> <span><xsl:value-of select="cv24:identite/cv24:genre"/></span></p>
                        </div>
                    </section>
                    <section class="cv-section">
                        <h2>Objectif</h2>
                        <div class="info-objectif">
                            <p><strong>Statut :</strong> <span><xsl:value-of select="cv24:objectif/@statut"/></span></p>
                            <p><xsl:value-of select="cv24:objectif"/></p>
                        </div>
                    </section>
                    <section class="cv-section">
                        <h2>Expériences Professionnelles</h2>
                        <ul class="experience-list">
                            <xsl:for-each select="cv24:prof/cv24:detail">
                                <li>
                                    <h3><xsl:value-of select="cv24:titre"/></h3>
                                    <p>Du <xsl:value-of select="cv24:datedeb"/> au <xsl:value-of select="cv24:datefin"/></p>
                                </li>
                            </xsl:for-each>
                        </ul>
                    </section>
                    <section class="cv-section">
                        <h2>Diplômes</h2>
                        <ul class="diplome-list">
                            <xsl:for-each select="cv24:competences/cv24:diplome">
                                <li>
                                    <h3><xsl:value-of select="cv24:titreD"/></h3>
                                    <p><xsl:value-of select="cv24:institut"/> (<xsl:value-of select="cv24:date"/>)</p>
                                </li>
                            </xsl:for-each>
                        </ul>
                    </section>
                    <section class="cv-section">
                        <h2>Certifications</h2>
                        <ul class="certification-list">
                            <xsl:for-each select="cv24:competences/cv24:certif">
                                <li>
                                    <h3><xsl:value-of select="cv24:titre"/></h3>
                                    <p>Du <xsl:value-of select="cv24:datedeb"/> au <xsl:value-of select="cv24:datefin"/></p>
                                </li>
                            </xsl:for-each>
                        </ul>
                    </section>
                    <section class="cv-section">
                        <h2>Langues</h2>
                        <ul class="langue-list">
                            <xsl:for-each select="cv24:divers/cv24:lv">
                                <li>
                                    <p><xsl:value-of select="@lang"/> (<xsl:value-of select="@cert"/> - Niveau <xsl:value-of select="@nivs"/>)</p>
                                </li>
                            </xsl:for-each>
                        </ul>
                    </section>
                    <section class="cv-section">
                        <h2>Autres</h2>
                        <ul class="autre-list">
                            <xsl:for-each select="cv24:divers/cv24:autre">
                                <li>
                                    <p><xsl:value-of select="@titre"/> : <xsl:value-of select="@comment"/></p>
                                </li>
                            </xsl:for-each>
                        </ul>
                    </section>
                </main>
            </div>
        </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

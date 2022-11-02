
(ns site.components.frontend.api
    (:require [site.components.frontend.sidebar.effects]
              [site.components.frontend.sidebar.subs]
              [site.components.frontend.copyright-label.views    :as copyright-label.views]
              [site.components.frontend.credits.views            :as credits.views]
              [site.components.frontend.mt-logo.views            :as mt-logo.views]
              [site.components.frontend.navbar.views             :as navbar.views]
              [site.components.frontend.sidebar.views            :as sidebar.views]
              [site.components.frontend.social-media-links.views :as social-media-links.views]
              [site.components.frontend.scroll-icon.views        :as scroll-icon.views]
              [site.components.frontend.scroll-sensor.views      :as scroll-sensor.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; site.components.frontend.credits.views
(def copyright-label copyright-label.views/component)

; site.components.frontend.credits.views
(def credits credits.views/component)

; site.components.frontend.mt-logo.views
(def mt-logo mt-logo.views/component)

; site.components.frontend.navbar.views
(def navbar navbar.views/component)

; site.components.frontend.sidebar.views
(def sidebar sidebar.views/component)

; site.components.frontend.scroll-icon.views
(def scroll-icon scroll-icon.views/component)

; site.components.frontend.scroll-sensor.views
(def scroll-sensor scroll-sensor.views/component)

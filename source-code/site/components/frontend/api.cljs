
(ns site.components.frontend.api
    (:require [site.components.frontend.language-selector.effects]
              [site.components.frontend.scheme-table.subs]
              [site.components.frontend.sidebar.effects]
              [site.components.frontend.sidebar.subs]
              [site.components.frontend.copyright-label.views    :as copyright-label.views]
              [site.components.frontend.credits.views            :as credits.views]
              [site.components.frontend.follow-us.views          :as follow-us.views]
              [site.components.frontend.language-selector.views  :as language-selector.views]
              [site.components.frontend.mt-logo.views            :as mt-logo.views]
              [site.components.frontend.navbar.views             :as navbar.views]
              [site.components.frontend.scheme-table.views       :as scheme-table.views]
              [site.components.frontend.sidebar.views            :as sidebar.views]
              [site.components.frontend.social-media-links.views :as social-media-links.views]
              [site.components.frontend.scroll-icon.views        :as scroll-icon.views]
              [site.components.frontend.scroll-sensor.views      :as scroll-sensor.views]
              [site.components.frontend.scroll-to-top.views      :as scroll-to-top.views]
              [site.components.frontend.slider.views             :as slider.views]
              
              [site.components.frontend.stepper.api              :as stepper]
              [site.components.frontend.tabs.views               :as tabs]
              [site.components.frontend.table.views              :as table.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; site.components.frontend.credits.views
(def copyright-label copyright-label.views/component)

; site.components.frontend.credits.views
(def credits credits.views/component)

; site.components.frontend.follow-us.views
(def follow-us follow-us.views/component)

; site.components.frontend.language-selector.views
(def language-selector language-selector.views/component)

; site.components.frontend.mt-logo.views
(def mt-logo mt-logo.views/component)

; site.components.frontend.navbar.views
(def navbar navbar.views/component)

; site.components.frontend.scheme-table.views
(def scheme-table scheme-table.views/component)

; site.components.frontend.sidebar.views
(def sidebar sidebar.views/component)

; site.components.frontend.scroll-icon.views
(def scroll-icon scroll-icon.views/component)

; site.components.frontend.scroll-sensor.views
(def scroll-sensor scroll-sensor.views/component)

; site.components.frontend.scroll-to-top.views
(def scroll-to-top scroll-to-top.views/component)

; site.components.frontend.slider.views
(def slider slider.views/component)

; site.components.frontend.stepper.api
(def stepper stepper/component)

; site.components.frontend.tabs.views
(def tabs tabs/component)

; site.components.frontend.tabs.views
(def table table.views/component)

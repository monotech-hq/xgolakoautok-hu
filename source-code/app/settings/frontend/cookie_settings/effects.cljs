
(ns app.settings.frontend.cookie-settings.effects
    (:require [settings.cookie-settings.views :as cookie-settings.views]
              [re-frame.api                   :as r]))

;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
; WARNING#0459

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.cookie-settings/render-settings!
  [:x.ui/render-popup! :settings.cookie-settings/view
                       {:body             #'cookie-settings.views/body
                        :header           #'cookie-settings.views/header
                        :horizontal-align :left
                        :user-close?      false}])

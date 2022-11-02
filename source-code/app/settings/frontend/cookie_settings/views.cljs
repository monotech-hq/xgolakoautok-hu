
(ns app.settings.frontend.cookie-settings.views
    (:require [elements.api :as elements]))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cancel-button
  []
  [elements/button ::cancel-button
                   {:preset   :cancel-button
                    :on-click [:ui/remove-popup! :settings.cookie-settings/view]}])

(defn save-button
  []
  [elements/button ::save-button
                   {:preset   :save-button
                    :variant  :transparent
                    :on-click {:dispatch-n [[:ui/remove-popup! :settings.cookie-settings/view]
                                            [:environment/cookie-settings-changed]]}}])

(defn header-label
  [])
  ;[elements/label {:content :privacy-settings :indent _}])

(defn header
  [_]
  [elements/horizontal-polarity ::header
                                {:start-content  [cancel-button]
                                 :middle-content [header-label]
                                 :end-content    [save-button]}])

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn privacy-policy-button
  []
  [elements/button ::policy-button
                   {:label :privacy-policy :preset :primary-button :layout :fit
                    :on-click [:router/go-to! "/@app-home/privacy-policy"]}])

(defn terms-of-service-button
  []
  [elements/button ::terms-of-service-button
                   {:label :terms-of-service :preset :primary-button :layout :fit
                    :on-click [:router/go-to! "/@app-home/terms-of-service"]}])

(defn body
  [_]
  [:<> ; This website uses cookies
       [elements/horizontal-separator {:size :s}]
       [elements/text {:content :this-website-uses-cookies :font-size :xs :layout :row :font-weight :bold}]
       ; Legal links
       [elements/horizontal-separator {:size :xxs}]
       [privacy-policy-button]
       [elements/horizontal-separator {:size :s}]
       [terms-of-service-button]
       ; Cookie settings
       [elements/horizontal-line {:color :highlight :layout :row}]
       ; BUG#1294
       ; Ha egyszerre jelenik meg a cookie-settings komponens a :settings.privacy-settings surface
       ; felületen és a :settings.cookie-consent popup felületen, akkor a popup bezárásakor az input-ok
       ; destructor függvényei törlik az inputok adatait a Re-Frame adatbázisból, ami miatt a ... surface
       ; felületen megjelenített inputok elveszítik az adataikat
       ; (kivéve ha különböző azonosítót kapnak a két felületen)
       [elements/switch ;::necessary-cookies-switch
                        {:disabled?     true
                         :initial-value true
                         :label         :necessary-cookies
                         :value-path [:environment :cookie-handler/meta-items :necessary-cookies-enabled?]}]

       ; BUG#1294
       [elements/switch ;::user-experience-cookies-switch
                        {:initial-value true
                         :label         :user-experience-cookies
                         :value-path [:environment :cookie-handler/meta-items :user-experience-cookies-enabled?]
                         :on-check   [:environment/cookie-settings-changed]
                         :on-uncheck [:environment/cookie-settings-changed]}]
       ; BUG#1294
       [elements/switch ;::analytics-cookies-switch
                        {:initial-value true
                         :label         :analytics-cookies
                         :value-path [:environment :cookie-handler/meta-items :analytics-cookies-enabled?]
                         :on-check   [:environment/cookie-settings-changed]
                         :on-uncheck [:environment/cookie-settings-changed]}]
       ; Remove stored cookies
       [elements/horizontal-separator {:size :s}]
       [elements/button {:label :remove-stored-cookies! :preset :secondary-button :layout :row
                         :on-click [:settings.remove-stored-cookies/render-dialog!]}]
       [elements/horizontal-separator {:size :s}]])

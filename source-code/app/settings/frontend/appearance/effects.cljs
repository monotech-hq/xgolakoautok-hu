
(ns app.settings.frontend.appearance.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :settings.appearance-settings/set-theme!
  ; @param (map) theme-props
  ;  {:id (keyword)
  ;   :name (metamorphic-content)}
  (fn [_ [_ theme-props]]
      (let [theme-id (get theme-props :id)]
          ;[:store-the-change-on-server-side! ...]
           [:x.ui/change-theme! theme-id])))

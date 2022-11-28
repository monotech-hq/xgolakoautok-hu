
(ns app.settings.frontend.appearance.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [_]
  [:<> [elements/radio-button ::selected-theme-radio-button
                              {:helper       "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                               :label        :selected-theme
                               :layout       :fit
                               :get-label-f  :name
                               ;:options-path (a/app-detail-path :app-themes)
                               :on-select    [:settings/set-theme!]}]
       [elements/horizontal-separator {:height :s}]])

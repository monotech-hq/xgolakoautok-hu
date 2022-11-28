
(ns app.settings.frontend.remove-stored-cookies.views
    (:require [elements.api :as elements]))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cancel-button
  []
  [elements/button ::cancel-button
                   {:color    :default
                    :label    :cancel!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click [:x.ui/remove-popup! :settings.remove-stored-cookies/view]}])

(defn remove-button
  []
  [elements/button ::remove-button
                   {:color    :warning
                    :label    :remove!
                    :preset   :close-button
                    :variant  :transparent
                    :on-click {:dispatch-n [[:x.ui/remove-popup! :settings.remove-stored-cookies/view]
                                            [:settings/remove-stored-cookies!]]}}])

(defn header
  [_]
  [elements/horizontal-polarity ::header
                                {:start-content [cancel-button]
                                 :end-content   [remove-button]}])

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [_]
  [:<> [elements/horizontal-separator {:height :xs}]
       [elements/text {:content :remove-stored-cookies? :font-weight :bold
                       :horizontal-align :center :icon :delete}]])


(ns app.settings.frontend.notifications.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [_]
  [:<> [elements/horizontal-separator {:height :l}]
       [elements/switch ::warning-bubbles-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :warning-bubbles
                         :layout     :fit
                         :disabled?  true
                         :default-value true}]
       [elements/horizontal-separator {:height :s}]
       [elements/switch ::notification-bubbles-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :notification-bubbles
                         :layout     :fit
                         :value-path [:a1]}]
       [elements/horizontal-separator {:height :l}]
       [elements/switch ::notification-sounds-switch
                        {:helper     "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
                         :label      :notification-sounds
                         :layout     :fit
                         :value-path [:a2]}]
       [elements/horizontal-separator {:height :s}]])

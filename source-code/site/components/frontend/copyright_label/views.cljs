
(ns site.components.frontend.copyright-label.views
    (:require [elements.api  :as elements]
              [random.api    :as random]
              [re-frame.api  :as r]
              [x.app-details :as x.app-details]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copyright-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [copyright-label]
  [_ {:keys [theme]}]
  (let [server-year          @(r/subscribe [:x.core/get-server-year])
        copyright-information (x.app-details/copyright-information server-year)]
       [elements/label ::copyright-label
                       {:color            (case theme :dark :invert :default)
                        :content          copyright-information
                        :font-size        :xs
                        :horizontal-align :center
                        :indent           {:bottom :xs :vertical :s}
                        :style            {:opacity ".6"}}]))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [copyright-label {...}]
  ;
  ; @usage
  ;  [copyright-label :my-copyright-label {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [copyright-label component-id component-props]))

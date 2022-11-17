
(ns app.components.frontend.popup-label-bar.views
    (:require [app.components.frontend.popup-label-bar.prototypes :as popup-label-bar.prototypes]
              [elements.api                                       :as elements]
              [random.api                                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-label-bar-label
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [elements/label {:content     label
                             :indent      {:horizontal :xs}
                             :line-height :block}]))

(defn- popup-secondary-button
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;  {:secondary-button (map)}
  [_ {:keys [secondary-button]}]
  [elements/button secondary-button])

(defn- popup-primary-button
  ; @param (keyword) popup-id
  ; @param (map) bar-props
  ;  {:primary-button (map)}
  [_ {:keys [primary-button]}]
  ; Ha szeretnéd, hogy a popup primary-button gombjának eseménye megtörténjen
  ; az ENTER billentyű lenyomására akkor is, ha egy szövegmező fókuszált
  ; állapotban van, amely állapot letiltja a {:required? false} keypress
  ; eseményeket, akkor az egyes mezők {:on-enter ...} tulajdonságaként
  ; is add meg az eseményt!
  [elements/button primary-button])

(defn- popup-label-bar
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  [elements/horizontal-polarity {:start-content  [popup-secondary-button bar-id bar-props]
                                 :middle-content [popup-label-bar-label  bar-id bar-props]
                                 :end-content    [popup-primary-button   bar-id bar-props]}])

(defn component
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ;  {:label (metamorphic-content)(opt)
  ;   :primary-button (map)(opt)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-even)}
  ;   :secondary-button (map)(opt)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-even)}}
  ;
  ; @usage
  ;  [popup-label-bar :my-popup {...}]
  ([bar-props]
   [component (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   (let [bar-props (popup-label-bar.prototypes/bar-props-prototype bar-props)]
        [popup-label-bar bar-id bar-props])))

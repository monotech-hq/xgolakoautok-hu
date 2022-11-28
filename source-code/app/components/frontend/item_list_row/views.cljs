
(ns app.components.frontend.item-list-row.views
    (:require [app.components.frontend.item-list-row.helpers    :as item-list-row.helpers]
              [app.components.frontend.item-list-row.prototypes :as item-list-row.prototypes]
              [css.api                                          :as css]
              [elements.api                                     :as elements]
              [random.api                                       :as random]
              [x.components.api                                 :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- row-border
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:cells (vector)}
  [_ {:keys [cells]}]
  [:tr {:style {:height "1px" :background-color "#f0f0f0"}}
       (letfn [(f [cells _] (conj cells [:td]))]
              (reduce f [:<>] cells))])

(defn- item-list-row
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:border (keyword)(opt)
  ;   :cells (components and/or symbols in vector)}
  [row-id {:keys [border cells] :as row-props}]
  [:<> (if (= border :top) [row-border row-id row-props])
       [:tr (item-list-row.helpers/row-attributes row-id row-props)
            (letfn [(f [cells cell-props]
                       (conj cells [x.components/content row-id cell-props]))]
                   (reduce f [:<>] cells))]
       (if (= border :bottom) [row-border row-id row-props])])

(defn component
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ;  {:border (keyword)(opt)
  ;    :bottom, :top}
  ;   :cells (metamorphic-contents in vector)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :drag-attributes (map)(opt)}
  ;
  ; @usage
  ;  [item-list-row {...}]
  ;
  ; @usage
  ;  [item-list-row :my-item-list-row {...}]
  ;
  ; @usage
  ;  [item-list-row :my-item-list-row {:cells [[:div ]]}]
  ([row-props]
   [component (random/generate-keyword) row-props])

  ([row-id row-props]
   (let [row-props (item-list-row.prototypes/row-props-prototype row-props)]
        [item-list-row row-id row-props])))

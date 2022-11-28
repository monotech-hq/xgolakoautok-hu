
(ns app.components.frontend.list-item-thumbnail.views
    (:require [elements.api :as elements]
              [random.api   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-thumbnail-body
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :thumbnail (string)(opt)}
  [_ {:keys [icon icon-family thumbnail]}]
  (cond icon-family      [:div {:style {:padding "24px 30px"}} [elements/icon {:icon icon :icon-family icon-family}]]
        icon             [:div {:style {:padding "24px 30px"}} [elements/icon {:icon icon}]]
        thumbnail        [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs} :uri thumbnail :width :l}]
        :empty-thumbnail [elements/thumbnail {:border-radius :s :height :s :indent {:horizontal :xxs}                :width :l}]))

(defn- list-item-thumbnail
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  [thumbnail-id thumbnail-props]
  ; BUG#0781 (source-code/app/components/frontend/item_list_table/views.cljs)
  [:td {:style {:width "84px"}}
       [list-item-thumbnail-body thumbnail-id thumbnail-props]])

(defn component
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;   :thumbnail (string)(opt)}
  ;
  ; @usage
  ;  [list-item-thumbnail {...}]
  ;
  ; @usage
  ;  [list-item-thumbnail :my-thumbnail {...}]
  ;
  ; @usage
  ;  [list-item-thumbnail {:thumbnail "/my-thumbnail.png"}]
  ;
  ; @usage
  ;  [list-item-thumbnail {:icon :people}]
  ([thumbnail-props]
   [component (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   [list-item-thumbnail thumbnail-id thumbnail-props]))

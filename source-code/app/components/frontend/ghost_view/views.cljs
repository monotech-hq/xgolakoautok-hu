
(ns app.components.frontend.ghost-view.views
    (:require [app.components.frontend.ghost-view.prototypes :as ghost-view.prototypes]
              [elements.api                                  :as elements]
              [random.api                                    :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- breadcrumbs-ghost
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;  {:breadcrumb-count (integer)(opt)}
  [_ {:keys [breadcrumb-count]}]
  [:div {:style {:display "flex" :grid-column-gap "12px" :padding-top "6px"}}
        (letfn [(f [%1 %2] (conj %1 [:div {:style {:width "80px"}} [elements/ghost {:height :xs}]]))]
               (reduce f [:<>] (range breadcrumb-count)))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-ghost
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;  {:item-count (integer)}
  [_ {:keys [item-count]}]
  [:div {:style {:padding "0 12px" :width "100%"}}
        [:div {:style {:display "flex" :flex-direction "column" :width "100%"}}
              (letfn [(f [%1 %2] (conj %1 [:div {:style {:flex-grow 1}} [elements/ghost {:height :xxl :indent {:horizontal :xs}}]]))]
                     (reduce f [:<>] (range item-count)))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- box-surface-search-bar-ghost
  ; @param (keyword) view-id
  ; @param (map) view-props
  [_ _]
  [:div {:style {:width "100%" :padding "12px 12px 48px 12px"}}
        [elements/ghost {:height :l}]])

(defn- box-surface-header-ghost
  ; @param (keyword) view-id
  ; @param (map) view-props
  [view-id view-props]
  [:div {:style {:padding "0 12px" :width "100%"}}
        [:div {:style {:padding-bottom "6px" :width "240px"}}
              [elements/ghost {:height :xl}]]
        [breadcrumbs-ghost view-id view-props]])

(defn- box-surface-body-ghost
  ; @param (keyword) view-id
  ; @param (map) view-props
  [view-id view-props]
  [:div {:style {:display "flex" :flex-direction "column" :width "100%" :grid-row-gap "24px"}}
        [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]
        [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]
        [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]])

(defn- box-surface-ghost
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;  {:breadcrumb-count (integer)(opt)}
  [view-id view-props]
  [:div {:style {:padding "0 12px" :width "100%"}}
        [:div {:style {:padding-bottom "6px" :width "240px"}}
              [elements/ghost {:height :xl}]]
        [breadcrumbs-ghost view-id view-props]
        [:div {:style {:width "100%" :height "96px"}}]
        [box-surface-body-ghost view-id view-props]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ghost-view-body
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;  {:layout (keyword)}
  [view-id {:keys [layout] :as view-props}]
  (case layout :box-surface            [box-surface-ghost            view-id view-props]
               :item-list              [item-list-ghost              view-id view-props]
               :box-surface-header     [box-surface-header-ghost     view-id view-props]
               :box-surface-body       [box-surface-body-ghost       view-id view-props]
               :box-surface-search-bar [box-surface-search-bar-ghost view-id view-props]))

(defn- ghost-view
  ; @param (keyword) view-id
  ; @param (map) view-props
  ;  {:indent (map)(opt)}
  [view-id {:keys [indent] :as view-props}]
  [elements/blank {:content [ghost-view-body view-id view-props]
                   :indent  indent}])

(defn component
  ; @param (keyword)(opt) view-id
  ; @param (map) view-props
  ;  {:box-count (integer)(opt)
  ;    TODO
  ;    W/ {:layout :box-surface}
  ;   :breadcrumb-count (integer)(opt)
  ;    W/ {:layout :box-surface, :box-surface-header}
  ;   :item-count (integer)(opt)
  ;    W/ {:layout :item-list :item-count 3
  ;   :layout (keyword)
  ;    :box-surface, box-surface-header, :item-list}
  ;
  ; @usage
  ;  [ghost-view {...}]
  ;
  ; @usage
  ;  [ghost-view :my-ghost-view {...}]
  ([view-props]
   [component (random/generate-keyword) view-props])

  ([view-id view-props]
   (let [];view-props (ghost-view.prototypes/view-props-prototype view-props)
        [ghost-view view-id view-props])))

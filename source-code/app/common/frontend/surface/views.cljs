
(ns app.common.frontend.surface.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- go-back-button
  [_]
  [:div {:style {:display :flex :justify-content :center}}
        [elements/button ::go-back-button
                         {:border-radius :s
                          :hover-color   :highlight
                          :indent        {:top :m}
                          :label         :back!
                          :on-click      [:x.router/go-back!]}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-box-layout-ghost-view
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;  {:breadcrumb-count (integer)}
  ;
  ; @usage
  ;  [surface-box-layout-ghost-view :my-editor {...}]
  [editor-id {:keys [breadcrumb-count]}]
  [:div {:style {:padding "0 12px" :width "100%"}}
        [:div {:style {:padding-bottom "6px" :width "240px"}}
              [elements/ghost {:height :xl}]]
        [:div {:style {:display "flex" :grid-column-gap "12px" :padding-top "6px"}}
              (letfn [(f [%1 %2] (conj %1 [:div {:style {:width "80px"}} [elements/ghost {:height :xs}]]))]
                     (reduce f [:<>] (range breadcrumb-count)))]
        [:div {:style {:display "flex" :flex-direction "column" :width "100%" :grid-row-gap "24px" :padding-top "96px"}}
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]
              [:div {:style {:flex-grow 1}} [elements/ghost {:height :5xl :indent {}}]]]])

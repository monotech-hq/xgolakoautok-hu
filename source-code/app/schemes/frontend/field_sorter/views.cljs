
(ns app.schemes.frontend.field-sorter.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.popup-a.api     :as popup-a]
              [map.api          :as map]
              [vector.api       :as vector]
              [re-frame.api            :as r]

              ; TEMP
              [plugins.dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-sorter-item-handle
  ; @param (keyword) scheme-id
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  ; @param (map) drag-props
  ;  {:handle-attributes (map)}
  [_ _ _ {:keys [handle-attributes]}]
  [:div handle-attributes [elements/icon {:color  :muted
                                          :height :l
                                          :icon   :drag_handle
                                          :indent {:horizontal :xxs :vertical :s}
                                          :style  {:cursor :grab}}]])

(defn- field-sorter-item-label
  ; @param (keyword) scheme-id
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  ;  {:field/name (metamorphic-content)}
  ; @param (map) drag-props
  [_ _ {:field/keys [name]} _]
  [elements/label {:content name
                   :indent {:right :s :horizontal :xxs}}])

(defn- field-sorter-item
  ; @param (keyword) scheme-id
  ; @param (integer) field-dex
  ; @param (namespaced map) field-item
  ; @param (map) drag-props
  ;  {:item-attributes (map)}
  [scheme-id field-dex field-item {:keys [item-attributes] :as drag-props}]
  [:div (update item-attributes :style merge {:display :flex})
        [field-sorter-item-handle scheme-id field-dex field-item drag-props]
        [field-sorter-item-label  scheme-id field-dex field-item drag-props]])

(defn- body
  ; @param (keyword) scheme-id
  [scheme-id]
  (let [scheme-fields @(r/subscribe [:schemes.form-handler/get-scheme-fields scheme-id])]
       [:<> [dnd-kit/body scheme-id
                          {:items            scheme-fields
                           :item-id-f        :field/field-id
                           :item-element     #'field-sorter-item
                           :on-order-changed (fn [_ _ %] (r/dispatch-sync [:schemes.field-sorter/reorder-fields! scheme-id %]))}]
            [elements/horizontal-separator {:size :s}]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @param (keyword) scheme-id
  [scheme-id]
  [:div {:style {:display :flex :justify-content :space-between}}
        [elements/label ::label
                        {:color   :muted
                         :content :reorder-fields!
                         :indent  {:left :s}}]
        [elements/icon-button ::close-icon-button
                              {:border-radius :s
                               :hover-color   :highlight
                               :keypress      {:key-code 27}
                               :on-click      [:x.ui/remove-popup! :schemes.field-sorter/view]
                               :preset        :close}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) scheme-id
  [scheme-id]
  [popup-a/layout :schemes.field-sorter/view
                  {:body   [body   scheme-id]
                   :header [header scheme-id]
                   :min-width :m}])

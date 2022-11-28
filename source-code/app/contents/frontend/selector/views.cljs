
(ns app.contents.frontend.selector.views
    (:require [app.common.frontend.api               :as common]
              [app.components.frontend.api           :as components]
              [app.contents.frontend.handler.helpers :as handler.helpers]
              [elements.api                          :as elements]
              [hiccup.api                            :as hiccup]
              [layouts.popup-a.api                   :as popup-a]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  ; @param (keyword) popup-id
  [_]
  (let [selected-content-count @(r/subscribe [:item-lister/get-selected-item-count :contents.selector])
        on-discard-selection [:item-lister/discard-selection! :contents.selector]]
       [common/item-selector-footer :contents.selector
                                    {:on-discard-selection on-discard-selection
                                     :selected-item-count  selected-content-count}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-list-item
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (integer) item-dex
  ; @param (map) content-item
  [selector-id _ item-dex {:keys [body name id]}]
  (let [content-body (-> body handler.helpers/parse-content-body hiccup/to-string)]
       [components/item-list-row {:cells [[components/list-item-thumbnail {:icon :article :icon-family :material-icons-outlined}]
                                          [components/list-item-cell      {:rows [{:content name :placeholder :unnamed-content}
                                                                                  {:content content-body :font-size :xs :color :muted}]}]
                                          [components/list-item-gap {:width 6}]
                                          [common/selector-item-marker selector-id item-dex {:item-id id}]
                                          [components/list-item-gap {:width 6}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; @param (keyword) popup-id
  [_]
  [:<> [elements/horizontal-separator {:height :xs}]
       [common/item-selector-body :contents.selector
                                  {:items-path        [:contents :selector/downloaded-items]
                                   :list-item-element #'content-list-item}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- control-bar
  []
  (let [selector-disabled? @(r/subscribe [:item-lister/lister-disabled? :contents.selector])]
       [common/item-selector-control-bar :contents.selector
                                         {:disabled?                selector-disabled?
                                          :search-field-placeholder :search-in-contents}]))

(defn- label-bar
  []
  (let [multi-select? @(r/subscribe [:item-lister/get-meta-item :contents.selector :multi-select?])]
       [common/item-selector-label-bar :contents.selector
                                       {:label    (if multi-select? :select-contents! :select-content!)
                                        :on-close [:x.ui/remove-popup! :contents.selector/view]}]))

(defn- header
  ; @param (keyword) popup-id
  [_]
  [:<> [label-bar]
       [control-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) popup-id
  [popup-id]
  [popup-a/layout :contents.selector/view
                  {:body                #'body
                   :header              #'header
                   :footer              #'footer
                   :min-width           :m
                   :stretch-orientation :vertical}])

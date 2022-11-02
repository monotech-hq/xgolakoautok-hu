
(ns app.contents.frontend.viewer.views
    (:require [app.common.frontend.api               :as common]
              [app.contents.frontend.handler.helpers :as handler.helpers]
              [elements.api                          :as elements]
              [engines.item-lister.api               :as item-lister]
              [engines.item-viewer.api               :as item-viewer]
              [forms.api                             :as forms]
              [layouts.surface-a.api                 :as surface-a]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-visibility
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-visibility @(r/subscribe [:db/get-item [:contents :viewer/viewed-item :visibility]])
        content-visibility  (case content-visibility :public :public-content :private :private-content)]
       [common/data-element ::content-visibility
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :content-visibility
                             :placeholder "-"
                             :value       content-visibility}]))

(defn- content-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])]
       [common/surface-box ::content-more-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [content-visibility]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-body
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-body     @(r/subscribe [:db/get-item [:contents :viewer/viewed-item :body]])
        content-body      (handler.helpers/parse-content-body content-body)]
       [common/data-element ::content-body
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :placeholder "-"
                             :value       content-body}]))

(defn- content-content-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])]
       [common/surface-box ::content-content-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [content-body]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-overview
  []
  [:<> [content-content-box]
       [content-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])]
       [common/item-viewer-menu-bar :contents.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}]}]))

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :contents.viewer])]
       (case current-view-id :overview [content-overview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-id       @(r/subscribe [:router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/contents/"content-id"/edit")]
       [common/item-viewer-controls :contents.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-name     @(r/subscribe [:db/get-item [:contents :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :contents.viewer/view
                                   {:crumbs [{:label :app-home   :route "/@app-home"}
                                             {:label :contents    :route "/@app-home/contents"}
                                             {:label content-name :placeholder :unnamed-content}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-name     @(r/subscribe [:db/get-item [:contents :viewer/viewed-item :name]])]
       [common/surface-label :contents.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       content-name
                              :placeholder :unnamed-content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- content-viewer
  []
  [item-viewer/body :contents.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:contents :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'content-viewer}])

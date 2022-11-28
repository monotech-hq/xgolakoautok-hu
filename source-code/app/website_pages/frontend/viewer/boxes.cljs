
(ns app.website-pages.frontend.viewer.boxes
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [app.contents.frontend.api   :as contents]
              [elements.api                :as elements]
              [forms.api                   :as forms]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-visibility
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])
        page-visibility  @(r/subscribe [:x.db/get-item [:website-pages :viewer/viewed-item :visibility]])
        page-visibility   (case page-visibility :public :public-page :private :private-page)]
       [components/data-element ::page-visibility
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :page-visibility
                                 :placeholder "n/a"
                                 :value       page-visibility}]))

(defn- page-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])]
       [components/surface-box ::page-more-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [page-visibility]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-content-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])
        page-content     @(r/subscribe [:x.db/get-item [:website-pages :viewer/viewed-item :content]])]
       [contents/content-preview ::page-content-preview
                                 {:color       :muted
                                  :disabled?   viewer-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :item-link   page-content
                                  :placeholder "n/a"}]))

(defn- page-content-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])]
       [components/surface-box ::page-content-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [page-content-preview]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-public-link
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])
        page-public-link @(r/subscribe [:x.db/get-item [:website-pages :viewer/viewed-item :public-link]])]
       [components/data-element ::page-public-link
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :public-link
                                 :placeholder "n/a"
                                 :value       page-public-link}]))

(defn- page-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])
        page-name        @(r/subscribe [:x.db/get-item [:website-pages :viewer/viewed-item :name]])]
       [components/data-element ::page-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       page-name}]))

(defn- page-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :website-pages.viewer])]
       [components/surface-box ::page-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [page-name]]]
                                              [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [page-public-link]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))

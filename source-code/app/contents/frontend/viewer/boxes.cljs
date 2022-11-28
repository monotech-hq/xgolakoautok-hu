
(ns app.contents.frontend.viewer.boxes
    (:require [app.common.frontend.api               :as common]
              [app.components.frontend.api           :as components]
              [app.contents.frontend.handler.helpers :as handler.helpers]
              [elements.api                          :as elements]
              [forms.api                             :as forms]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-visibility
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-visibility @(r/subscribe [:x.db/get-item [:contents :viewer/viewed-item :visibility]])
        content-visibility  (case content-visibility :public :public-content :private :private-content)]
       [components/data-element ::content-visibility
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :content-visibility
                                 :placeholder "n/a"
                                 :value       content-visibility}]))

(defn- content-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])]
       [components/surface-box ::content-more-data-box
                               {:indent  {:top :m}
                                :content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [content-visibility]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-body
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-body     @(r/subscribe [:x.db/get-item [:contents :viewer/viewed-item :body]])
        content-body      (handler.helpers/parse-content-body content-body)]
       ; XXX#0516 (source-code/app/common/frontend/data_element/views.cljs)
       ; Mivel a data-element value tulajdonságának értékét lehetséges vektorban felsorolot
       ; metamorphic-content típusokként is megadni, ezért a hiccup típust félreértené
       ; és megpróbálná feldarabolni, emiatt szükséges egy vektorba helyezve átadni
       ; a hiccup tartalmat a data-element komponensnek!
       [components/data-element ::content-body
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :placeholder "n/a"
                                 :value       [content-body]}]))

(defn- content-content-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])]
       [components/surface-box ::content-content-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [content-body]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :indent    {:top :m}
                                :label     :content}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])
        content-name     @(r/subscribe [:x.db/get-item [:contents :viewer/viewed-item :name]])]
       [components/data-element ::content-name
                                {:disabled?   viewer-disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       :name
                                 :placeholder "n/a"
                                 :value       content-name}]))

(defn- content-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :contents.viewer])]
       [components/surface-box ::content-data-box
                               {:content [:<> [:div (forms/form-row-attributes)
                                                    [:div (forms/form-block-attributes {:ratio 100})
                                                          [content-name]]]
                                              [elements/horizontal-separator {:height :s}]]
                                :disabled? viewer-disabled?
                                :label     :data}]))

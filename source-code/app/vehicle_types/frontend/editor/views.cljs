
(ns app.vehicle-types.frontend.editor.views
    (:require [app.common.frontend.api                    :as common]
              [app.components.frontend.api                :as components]
              [app.schemes.frontend.api                   :as schemes]
              [app.vehicle-types.frontend.editor.boxes    :as editor.boxes]
              [app.vehicle-types.frontend.handler.queries :as handler.queries]
              [elements.api                               :as elements]
              [layouts.surface-a.api                      :as surface-a]
              [re-frame.api                               :as r]
              [vector.api                                 :as vector]
              [x.components.api                           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :vehicle-types.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-technical-data
  []
  [:<> [editor.boxes/type-technical-data-box]])

(defn- type-images
  []
  [:<> [editor.boxes/type-images-box]])

(defn- type-files
  []
  [:<> [editor.boxes/type-files-box]])

(defn- type-price
  []
  [:<> [editor.boxes/type-price-box]])

(defn- type-data
  []
  [:<> [editor.boxes/type-basic-data]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-types.editor])]
       (case current-view-id :data           [type-data]
                             :images         [type-images]
                             :files          [type-files]
                             :price          [type-price]
                             :technical-data [type-technical-data])))

(defn- body
  ; @param (keyword) editor-id
  ; @param (map) server-response
  [_ _]
  (let [model-id         @(r/subscribe [:x.router/get-current-route-path-param :model-id])
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :vehicle-types.technical-data])]
       [common/item-editor-body :vehicle-types.editor
                                {:form-element     [view-selector]
                                 :initial-item     {:model-id model-id}
                                 :item-path        [:vehicle-types :editor/edited-item]
                                 :query            (handler.queries/request-model-name-query)
                                 :suggestion-keys  (vector/concat-items scheme-field-ids
                                                                        [:name :manufacturer-price :price-margin :dealer-rebate])
                                 :suggestions-path [:vehicle-types :editor/suggestions]}]))

(defn- preloader
  []
  ; A body komponens scheme-field-ids változójának értékéhez szükséges előre
  ; letölteni a request-scheme-form-query lekérést!
  [x.components/querier :vehicle-types.editor/preloader
                        {:content     [body]
                         :placeholder [components/ghost-view {:breadcrumb-count 4 :layout :box-surface}]
                         :query       (schemes/request-scheme-form-query :vehicle-types.technical-data)}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [type-name        @(r/subscribe [:x.db/get-item [:vehicle-types :editor/edited-item :name]])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-types :handler/model-item :name]])
        type-id          @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :model-id])
        type-route       @(r/subscribe [:item-editor/get-item-route :vehicle-types.editor type-id])
        model-route       (str "/@app-home/vehicle-models/"model-id"/types")
        scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids :vehicle-types.technical-data])]
       [common/item-editor-header :vehicle-types.editor
                                  {:label       type-name
                                   :placeholder :unnamed-vehicle-type
                                   :crumbs      [{:label :app-home       :route "/@app-home"}
                                                 {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                                 {:label model-name      :route model-route :placeholder :unnamed-vehicle-model}
                                                 {:label type-name       :route type-route  :placeholder :unnamed-vehicle-type}]
                                   :menu-items  [{:label :data           :change-keys [:name]}
                                                 {:label :images         :change-keys [:images]}
                                                 {:label :files          :change-keys [:files]}
                                                 {:label :price          :change-keys [:manufacturer-price :price-margin :dealer-rebate]}
                                                 {:label :technical-data :change-keys scheme-field-ids}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [preloader]
                                   [footer]]}])


(ns app.rental-vehicles.frontend.editor.views
    (:require [app.common.frontend.api   :as common]
              [app.contents.frontend.api :as contents]
              [app.storage.frontend.api  :as storage]
              [elements.api              :as elements]
              [engines.item-editor.api   :as item-editor]
              [forms.api                 :as forms]
              [layouts.surface-a.api     :as surface-a]
              [re-frame.api              :as r]
              [time.api                  :as time]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-public-link
  []
  (let [editor-disabled?    @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])
        vehicle-public-link @(r/subscribe [:rental-vehicles.editor/get-vehicle-public-link])]
       [common/data-element ::vehicle-public-link
                            {:disabled? editor-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :public-link
                             :value     vehicle-public-link}]))

(defn- vehicle-visibility-radio-button
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/radio-button ::vehicle-visibility-radio-button
                              {:disabled?       editor-disabled?
                               :indent          {:top :m :vertical :s}
                               :label           :visibility-on-the-website
                               :options         [{:label :public-content  :helper :visible-to-everyone     :value :public}
                                                 {:label :private-content :helper :only-visible-to-editors :value :private}]
                               :option-helper-f :helper
                               :option-label-f  :label
                               :option-value-f  :value
                               :value-path      [:rental-vehicles :editor/edited-item :visibility]}]))

(defn- vehicle-settings-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/surface-box ::vehicle-settings-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [vehicle-visibility-radio-button]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [vehicle-public-link]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :settings}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-settings
  []
  [:<> [vehicle-settings-box]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-description-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [contents/content-picker ::vehicle-description-picker
                                {:autosave?     true
                                 :disabled?     editor-disabled?
                                 :indent        {:vertical :s}
                                 :multi-select? false
                                 :placeholder   "-"
                                 :value-path    [:rental-vehicles :editor/edited-item :description]}]))

(defn- vehicle-description-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/surface-box ::vehicle-description-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [vehicle-description-picker]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :description}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-content
  []
  [:<> [vehicle-description-box]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-construction-year-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])
        current-year      (time/get-year)]
       [elements/text-field ::vehicle-construction-year-field
                            {:disabled?   editor-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :construction-year
                             :placeholder current-year
                             :value-path  [:rental-vehicles :editor/edited-item :construction-year]}]))

(defn- vehicle-number-of-seats-counter
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/counter ::vehicle-number-of-seats-counter
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :s}
                             :label      :number-of-seats
                             :min-value  0
                             :max-value  10
                             :value-path [:rental-vehicles :editor/edited-item :number-of-seats]}]))

(defn- vehicle-number-of-bunks-counter
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/counter ::vehicle-number-of-bunks-counter
                            {:disabled?  editor-disabled?
                             :indent     {:top :m :vertical :s}
                             :label      :number-of-bunks
                             :min-value  0
                             :max-value  10
                             :value-path [:rental-vehicles :editor/edited-item :number-of-bunks]}]))

(defn- vehicle-technical-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/surface-box ::vehicle-technical-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 40})
                                                      [vehicle-construction-year-field]]
                                                [:div (forms/form-block-attributes {:ratio 10})]
                                                [:div (forms/form-block-attributes {:ratio 25})
                                                      [vehicle-number-of-seats-counter]]
                                                [:div (forms/form-block-attributes {:ratio 25})
                                                      [vehicle-number-of-bunks-counter]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :technical-data}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-image-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [storage/media-picker ::vehicle-image-picker
                             {:autosave?     false
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? true
                              :placeholder   "-"
                              :sortable?     true
                              :toggle-label  :select-images!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:rental-vehicles :editor/edited-item :images]}]))

(defn- vehicle-images-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/surface-box ::vehicle-images-box
                           {:content [:<> [vehicle-image-picker]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :images}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-images
  []
  [:<> [vehicle-images-box]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-thumbnail-picker
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [storage/media-picker ::vehicle-thumbnail-picker
                             {:autosave?     true
                              :disabled?     editor-disabled?
                              :extensions    ["bmp" "jpg" "jpeg" "png" "webp"]
                              :indent        {:vertical :s}
                              :multi-select? false
                              :placeholder   "-"
                              :toggle-label  :select-image!
                              :thumbnail     {:height :3xl :width :5xl}
                              :value-path    [:rental-vehicles :editor/edited-item :thumbnail]}]))

(defn- vehicle-thumbnail-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/surface-box ::vehicle-thumbnail-box
                           {:content [:<> [vehicle-thumbnail-picker]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :thumbnail}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-thumbnail
  []
  [:<> [vehicle-thumbnail-box]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-name-field
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/combo-box ::vehicle-name-field
                           {:autofocus?   true
                            :disabled?    editor-disabled?
                            :indent       {:top :m :vertical :s}
                            :label        :name
                            :options-path [:rental-vehicles :editor/suggestions :name]
                            :placeholder  :vehicle-name-placeholder
                            :value-path   [:rental-vehicles :editor/edited-item :name]}]))

(defn- vehicle-type-select
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [elements/select ::vehicle-type-select
                        {:disabled?     editor-disabled?
                         :indent        {:top :m :vertical :s}
                         :label         :type
                         :layout        :select
                         :options-label :vehicle-type
                         :options       [:alcove-rv :semi-integrated-rv :van-rv :caravan :trailer]
                         :value-path    [:rental-vehicles :editor/edited-item :type]}]))

(defn- vehicle-basic-data-box
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/surface-box ::vehicle-basic-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 60})
                                                      [vehicle-name-field]]
                                                [:div (forms/form-block-attributes {:ratio 40})
                                                      [vehicle-type-select]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? editor-disabled?
                            :label     :basic-data}]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- vehicle-data
  []
  [:<> [vehicle-basic-data-box]
       [vehicle-technical-data-box]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :rental-vehicles.editor])]
       (case current-view-id :data      [vehicle-data]
                             :content   [vehicle-content]
                             :thumbnail [vehicle-thumbnail]
                             :images    [vehicle-images]
                             :settings  [vehicle-settings])))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/item-editor-menu-bar :rental-vehicles.editor
                                    {:disabled?  editor-disabled?
                                     :menu-items [{:label :data      :change-keys [:name :type :construction-year
                                                                                   :number-of-bunks :number-of-seats]}
                                                  {:label :thumbnail :change-keys [:thumbnail]}
                                                  {:label :images    :change-keys [:images]}
                                                  {:label :content   :change-keys [:description]}
                                                  {:label :settings  :change-keys [:visibility]}]}]))

(defn- controls
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])]
       [common/item-editor-controls :rental-vehicles.editor
                                    {:disabled? editor-disabled?}]))

(defn- breadcrumbs
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])
        vehicle-name     @(r/subscribe [:x.db/get-item [:rental-vehicles :editor/edited-item :name]])]
       [common/surface-breadcrumbs :rental-vehicles.editor/view
                                   {:crumbs [{:label :app-home    :route "/@app-home"}
                                             {:label :rental-vehicles    :route "/@app-home/rental-vehicles"}
                                             {:label vehicle-name :placeholder :unnamed-vehicle}]
                                    :disabled? editor-disabled?}]))

(defn- label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :rental-vehicles.editor])
        vehicle-name     @(r/subscribe [:x.db/get-item [:rental-vehicles :editor/edited-item :name]])]
       [common/surface-label :rental-vehicles.editor/view
                             {:disabled?   editor-disabled?
                              :label       vehicle-name
                              :placeholder :unnamed-vehicle}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- vehicle-editor
  []
  (let [current-year (time/get-year)]
       [item-editor/body :rental-vehicles.editor
                         {:auto-title?      true
                          :form-element     #'view-structure
                          :error-element    [common/error-content {:error :the-item-you-opened-may-be-broken}]
                          :ghost-element    #'common/item-editor-ghost-element
                          :initial-item     {:construction-year current-year
                                             :number-of-bunks   0
                                             :number-of-seats   0
                                             :type              :alcove-rv
                                             :visibility        :public}
                          :item-path        [:rental-vehicles :editor/edited-item]
                          :label-key        :name
                          :suggestion-keys  [:name]
                          :suggestions-path [:rental-vehicles :editor/suggestions]}]))

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'vehicle-editor}])

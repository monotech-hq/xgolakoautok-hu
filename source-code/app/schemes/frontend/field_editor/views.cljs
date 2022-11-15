
(ns app.schemes.frontend.field-editor.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.popup-a.api     :as popup-a]
              [re-frame.api            :as r]))

;; -- XXX#5561 ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A mezők csoportosítása még nincs kidolgozva, ezért jelenleg a mezőket
; csoportok nélkül lehetséges hozzáadni!

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-bar
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id field-id]
  (let [field-name @(r/subscribe [:x.db/get-item [:schemes :field-editor/field-item :field/name]])]
       ; XXX#5561
       ;field-group @(r/subscribe [:x.db/get-item [:schemes :field-editor/field-item :field/group]])
       [common/popup-label-bar :schemes.field-editor/view
                               {:primary-button   {:disabled? (empty? field-name)
                                                  ; XXX#5561
                                                  ;:disabled? (or (empty? field-name) (empty? field-group))
                                                   :on-click [:schemes.field-editor/save-field! scheme-id field-id]
                                                   :label    (if field-id :save! :create!)
                                                   :keypress {:key-code 13 :required? true}}
                                :secondary-button {:on-click [:x.ui/remove-popup! :schemes.field-editor/view]
                                                   :label    :cancel!
                                                   :keypress {:key-code 27 :required? true}}
                                :label            (if field-id :edit-field! :add-field!)}]))

(defn- header
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id field-id]
  (let [editor-status @(r/subscribe [:x.db/get-item [:schemes :field-editor/meta-items :editor-status]])]
       (case editor-status :saving      [:<>]
                           :save-failed [label-bar scheme-id field-id]
                                        [label-bar scheme-id field-id])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-type-select
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ _]
  [elements/select ::field-group-select
                   {:indent          {:bottom :xs :top :m :vertical :xs}
                    :initial-options [:single-field :multi-field]
                    :initial-value   :single-field
                    :label           :type
                    :required?       true
                    :value-path      [:schemes :field-editor/field-item :field/type]}])

(defn- field-unit-field
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ _]
  [elements/text-field ::field-unit-field
                       {:indent     {:bottom :xs :top :m :vertical :xs}
                        :label      :unit-label
                        :value-path [:schemes :field-editor/field-item :field/unit]}])

(defn- field-name-field
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ _]
  [elements/text-field ::field-name-field
                       {:autofocus?  true
                        :indent      {:top :m :vertical :xs}
                        :label       :field-name
                        :required?   true
                        :value-path  [:schemes :field-editor/field-item :field/name]}])

(defn- field-group-select
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id _]
  (let [scheme-field-groups @(r/subscribe [:schemes.form-handler/get-scheme-field-groups scheme-id])]
       [elements/select ::field-group-select
                        {:extendable?              true
                         :indent                   {:bottom :xs :top :m :vertical :xs}
                         :initial-options          scheme-field-groups
                         :label                    :field-group
                         :option-field-placeholder :add-field-group!
                         :required?                true
                         :value-path               [:schemes :field-editor/field-item :field/group]}]))

(defn- save-failed-label
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ _]
  (let [editor-status @(r/subscribe [:x.db/get-item [:schemes :field-editor/meta-items :editor-status]])]
       (case editor-status :saving      [:<>]
                           :save-failed [elements/label ::save-failed-label
                                                        {:content          :failed-to-save
                                                         :color            :warning
                                                         :horizontal-align :center
                                                         :line-height      :block}]
                                        [:<>])))

(defn- field-no-label
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ field-id]
  (if field-id (let [field-no @(r/subscribe [:x.db/get-item [:schemes :field-editor/field-item :field/field-no]])]
                    [elements/label ::field-no-label
                                    {:color       :muted
                                     :content     {:content :field-id-n :replacements [field-no]}
                                     :font-size   :xs
                                     :indent      {:bottom :xxs :right :xs}
                                     :line-height :block}])))

(defn- field-name-label
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ _]
  (let [field-name  @(r/subscribe [:x.db/get-item [:schemes :field-editor/backup-item :field/name]])]
       ; XXX#5561
       ;field-group @(r/subscribe [:x.db/get-item [:schemes :field-editor/backup-item :field/group]])
       [elements/label ::field-name-label
                       {:color       :muted
                       ; XXX#5561
                       ;:content     (str field-group " / " field-name)
                        :content     field-name
                        :font-size   :xs
                        :indent      {:bottom :xxs :left :xs}
                        :line-height :block}]))

(defn- field-info
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id field-id]
  (if field-id [elements/horizontal-polarity ::field-info
                                             {:start-content [field-name-label scheme-id field-id]
                                              :end-content   [field-no-label   scheme-id field-id]}]))

(defn- form
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id field-id]
  [:<> [save-failed-label  scheme-id field-id]
       [field-name-field   scheme-id field-id]
       [field-type-select  scheme-id field-id]
       [field-unit-field   scheme-id field-id]
      ; XXX#5561
      ;[field-group-select scheme-id field-id]
       [field-info         scheme-id field-id]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- saving-label
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [_ field-id]
  [common/popup-progress-indicator :schemes.field-editor/view
                                   {:label  (if field-id :saving-field... :adding-field...)
                                    :indent {:horizontal :xxl}}])

(defn- body
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id field-id]
  (let [editor-status @(r/subscribe [:x.db/get-item [:schemes :field-editor/meta-items :editor-status]])]
       (case editor-status :saving      [saving-label scheme-id field-id]
                           :save-failed [form         scheme-id field-id]
                                        [form         scheme-id field-id])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) scheme-id
  ; @param (keyword or nil) field-id
  [scheme-id field-id]
  [popup-a/layout :schemes.field-editor/view
                  {:body      [body   scheme-id field-id]
                   :header    [header scheme-id field-id]
                   :min-width :s}])

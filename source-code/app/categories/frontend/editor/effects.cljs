
(ns app.categories.frontend.editor.effects
    (:require [app.categories.frontend.editor.views :as editor.views]
              [re-frame.api                         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :categories.editor/load-editor!
  {:dispatch-n [[:gestures/init-view-handler! :categories.editor
                                              {:default-view-id :data}]
                [:ui/render-surface! :categories.editor/view
                                     {:content #'editor.views/view}]]})

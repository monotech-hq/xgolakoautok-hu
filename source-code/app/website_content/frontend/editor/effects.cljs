
(ns app.website-content.frontend.editor.effects
  (:require [app.website-content.frontend.editor.views :as editor.views]
            [re-frame.api                              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-content.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-content.editor
                                                {:default-view-id :basic-data}]
                [:website-content.editor/render-editor!]]})

(r/reg-event-fx :website-content.editor/render-editor!
  [:x.ui/render-surface! :website-content.editor/view
                         {:content #'editor.views/view}])

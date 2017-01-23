// Queue är en kö med metoderna Put, Get, IsEmpty och Empty.

class ListNode
{
    WordRec element;
    ListNode next = null;
}

class Queue
{
    private ListNode front = null, back = null;

    public void Put(WordRec element)
    { 
		if (IsEmpty()) back = front = new ListNode();
		else back = back.next = new ListNode();
		back.element = element;
    }

    public WordRec Get()
    { 
		if (IsEmpty()) return null;
		WordRec element = front.element;
		front = front.next;
		return element;
    }

    public boolean IsEmpty()
    { return front == null; }

    public void Empty()
    { front = back = null; }
}
